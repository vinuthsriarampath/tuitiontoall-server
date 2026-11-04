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

import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.messaging.dtos.request.SendMessageRequest;
import edu.vinu.domain.messaging.dtos.response.ChatMessageResponse;
import edu.vinu.domain.messaging.entity.Conversation;
import edu.vinu.domain.messaging.entity.Message;
import edu.vinu.domain.messaging.entity.MessageAttachment;
import edu.vinu.domain.messaging.enums.MessageDeliveryStatus;
import edu.vinu.domain.messaging.mapper.MessageMapper;
import edu.vinu.domain.messaging.repository.ConversationRepository;
import edu.vinu.domain.messaging.repository.MessageRepository;
import edu.vinu.domain.messaging.services.MessagingService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {
    private final UserService userService;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChatMessageResponse saveAndProcessMessage(String senderEmail, SendMessageRequest request) {
        UserEntity sender = userService.getUserEntityByEmail(senderEmail);

        Conversation conversation = conversationRepository.findById(request.conversationId()).orElseThrow(() -> new NotFoundException("Conversation not found with id: " + request.conversationId()));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(request.content())
                .deliveryStatus(MessageDeliveryStatus.SENT)
                .isEdited(false)
                .isDeleted(false)
                .build();

        if (request.attachments() != null && !request.attachments().isEmpty()) {
            List<MessageAttachment> attachmentEntities = request.attachments().stream().map(att ->
                    MessageAttachment.builder()
                            .message(message)
                            .fileUrl(att.fileUrl())
                            .fileName(att.fileName())
                            .fileType(att.fileType())
                            .fileSize(att.fileSize())
                            .build()
            ).toList();
            message.getAttachments().addAll(attachmentEntities);
        }

        Message savedMessage = messageRepository.save(message);

        return MessageMapper.toChatMessageResponse(savedMessage);
    }

}
