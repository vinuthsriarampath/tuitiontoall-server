/*
 * Copyright (c) 2026 vinuth sri arampath
 *
 * This code is the intellectual property of vinuth sri arampath and is protected under copyright law.
 * Unauthorized copying, modification, distribution, or use of this code, in whole or in part,
 * without prior written permission is strictly prohibited.
 *
 * Portions of this code may be generated with AI and modified by vinuth sri arampath
 * All rights reserved.
 *
 *
 */

package edu.vinu.domain.messaging.services.impl;

import edu.vinu.common.exception.custom.BadRequestException;
import edu.vinu.common.exception.custom.InternalServerErrorException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.domain.messaging.dtos.request.EditMessageRequest;
import edu.vinu.domain.messaging.dtos.request.SendMessageRequest;
import edu.vinu.domain.messaging.dtos.response.ChatMessageResponse;
import edu.vinu.domain.messaging.entity.Conversation;
import edu.vinu.domain.messaging.entity.ConversationParticipant;
import edu.vinu.domain.messaging.entity.Message;
import edu.vinu.domain.messaging.entity.MessageAttachment;
import edu.vinu.domain.messaging.enums.MessageDeliveryStatus;
import edu.vinu.domain.messaging.enums.MessageDeliveryStatusUpdate;
import edu.vinu.domain.messaging.events.ConversationMessageReceivedEvent;
import edu.vinu.domain.messaging.mapper.MessageMapper;
import edu.vinu.domain.messaging.repository.ConversationParticipantRepository;
import edu.vinu.domain.messaging.repository.ConversationRepository;
import edu.vinu.domain.messaging.repository.MessageRepository;
import edu.vinu.domain.messaging.services.ChatAttachmentService;
import edu.vinu.domain.messaging.services.MessagingService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {
    private final UserService userService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final ConversationParticipantRepository participantRepository;
    private final ChatAttachmentService attachmentService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ChatMessageResponse saveAndProcessMessage(String senderEmail, SendMessageRequest request) {
        UserEntity sender = userService.getUserEntityByEmail(senderEmail);

        Conversation conversation = conversationRepository.findById(request.conversationId())
                .orElseThrow(() -> new NotFoundException(
                        "Conversation not found with id: " + request.conversationId()
                ));

        // Verify that the sender is a participant.
        ConversationParticipant senderParticipant =
                participantRepository.findByConversationIdAndUserId(
                        conversation.getId(),
                        sender.getId()
                );

        if (senderParticipant == null) {
            throw new UnauthorizedException("You are not a participant of this conversation");
        }

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(request.content())
                .deliveryStatus(MessageDeliveryStatus.SENT)
                .isEdited(false)
                .isDeleted(false)
                .build();

        // Process attachments.
        if (request.attachments() != null && !request.attachments().isEmpty()) {

            List<MessageAttachment> attachmentEntities = request.attachments()
                    .stream()
                    .map(att -> MessageAttachment.builder()
                            .message(message)
                            .fileUrl(att.fileUrl())
                            .fileName(att.fileName())
                            .fileType(att.fileType())
                            .fileSize(att.fileSize())
                            .build()
                    )
                    .toList();

            message.getAttachments().addAll(attachmentEntities);
        }

        Message savedMessage;

        List<String> successfullyMovedFiles = new ArrayList<>();

        try {
            savedMessage = messageRepository.save(message);

            savedMessage.getAttachments().forEach(attachment -> {
                attachmentService.moveToPermanentStorage(attachment.getFileUrl());
                successfullyMovedFiles.add(attachment.getFileUrl());
            });

        } catch (Exception e) {

            successfullyMovedFiles.forEach(
                    attachmentService::moveBackToTempStorage
            );

            throw new InternalServerErrorException("Error occurred while saving and processing message");
        }

        // Update sender's read timestamp.
        senderParticipant.setLastReadAt(LocalDateTime.now());
        participantRepository.save(senderParticipant);

        ChatMessageResponse response = MessageMapper.toChatMessageResponse(savedMessage);

        // Publish an event for every other participant.
        List<Long> participantIds = participantRepository.findUserIdsByConversationId(conversation.getId());

        for (Long recipientId : participantIds) {

            if (recipientId.equals(sender.getId())) {
                continue;
            }

            eventPublisher.publishEvent(new ConversationMessageReceivedEvent(recipientId, conversation.getId(), savedMessage.getId()));
        }

        return response;
    }

    @Override
    @Transactional
    public void updateMessagesStatus(Long conversationId, List<Long> messageIds, MessageDeliveryStatusUpdate status) {
        if (messageIds == null || messageIds.isEmpty()) return;

        List<Message> messagesToUpdate = messageRepository.findAllById(messageIds);

        for (Message message : messagesToUpdate) {
            if (!message.getConversation().getId().equals(conversationId)) {
                throw new IllegalArgumentException("Message with ID " + message.getId() + " does not belong to conversation " + conversationId);
            }
            message.setDeliveryStatus(status.equals(MessageDeliveryStatusUpdate.DELIVERED) ? MessageDeliveryStatus.DELIVERED : MessageDeliveryStatus.READ);
        }
        messageRepository.saveAll(messagesToUpdate);
    }

    @Override
    @Transactional
    public ChatMessageResponse deleteMessage(String senderEmail, Long messageId) {
        UserEntity user = userService.getUserEntityByEmail(senderEmail);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found"));

        // Verify sender ownership
        if (!message.getSender().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only delete your own messages");
        }

        message.setIsDeleted(true);
        message.setContent("This message was deleted");

        List<MessageAttachment> attachmentsToBeDeleted = new ArrayList<>(message.getAttachments());
        message.getAttachments().clear();

        Message saved;
        try {
            saved = messageRepository.save(message);
            attachmentsToBeDeleted.forEach(atc -> attachmentService.deleteAttachment(atc.getFileUrl()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return MessageMapper.toChatMessageResponse(saved);
    }

    @Override
    @Transactional
    public ChatMessageResponse editMessage(String senderEmail, EditMessageRequest request) {
        UserEntity user = userService.getUserEntityByEmail(senderEmail);

        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new NotFoundException("Message not found"));

        if (!message.getSender().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only edit your own messages");
        }

        if (message.getIsDeleted()) {
            throw new BadRequestException("Cannot edit a deleted message");
        }

        message.setContent(request.newContent());
        message.setIsEdited(true);

        Message saved = messageRepository.save(message);
        return MessageMapper.toChatMessageResponse(saved);
    }

}
