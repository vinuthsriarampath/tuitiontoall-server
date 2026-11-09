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

import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.domain.messaging.dtos.request.ConversationFilterRequest;
import edu.vinu.domain.messaging.dtos.response.ChatMessageResponse;
import edu.vinu.domain.messaging.dtos.response.ConversationResponse;
import edu.vinu.domain.messaging.entity.Conversation;
import edu.vinu.domain.messaging.entity.ConversationParticipant;
import edu.vinu.domain.messaging.enums.ConversationParticipantRole;
import edu.vinu.domain.messaging.enums.ConversationType;
import edu.vinu.domain.messaging.mapper.ConversationMapper;
import edu.vinu.domain.messaging.mapper.MessageMapper;
import edu.vinu.domain.messaging.repository.ConversationParticipantRepository;
import edu.vinu.domain.messaging.repository.ConversationRepository;
import edu.vinu.domain.messaging.repository.MessageRepository;
import edu.vinu.domain.messaging.services.ConversationService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.repository.UserRepository;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<ConversationResponse> getUserConversations(ConversationFilterRequest filters) {
        UserEntity currentUser = userService.getCurrentUser();

        List<Conversation> conversations = conversationRepository.findAllByUserId(currentUser.getId(), filters.searchTerm());

        return conversations.stream().map(conv -> {
            long unreadCount = participantRepository.countUnreadMessages(conv.getId(), currentUser.getId());
            ConversationResponse conversationResponse = ConversationMapper.toConversationResponse(conv, currentUser);
            conversationResponse.setUnreadCount(unreadCount);
            return conversationResponse;
        }).toList();
    }

    @Override
    @Transactional
    public ConversationResponse getOrCreateDirectConversation(Long targetUserId) {
        UserEntity currentUser = userService.getCurrentUser();

        if (currentUser.getId().equals(targetUserId)) {
            throw new IllegalArgumentException("Cannot start a conversation with yourself");
        }

        UserEntity targetUser = userService.getUserEntityById(targetUserId);

        Conversation existingConv = conversationRepository.findDirectConversationBetweenUsers(currentUser.getId(), targetUser.getId());

        if (existingConv != null) {
            return ConversationMapper.toConversationResponse(existingConv, currentUser);
        }

        Conversation newConv = Conversation.builder()
                .type(ConversationType.DIRECT)
                .createdBy(currentUser)
                .createdDate(LocalDateTime.now())
                .build();

        Conversation savedConv = conversationRepository.save(newConv);

        ConversationParticipant participant1 = ConversationParticipant.builder()
                .conversation(savedConv)
                .user(currentUser)
                .role(ConversationParticipantRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build();

        ConversationParticipant participant2 = ConversationParticipant.builder()
                .conversation(savedConv)
                .user(targetUser)
                .role(ConversationParticipantRole.MEMBER)
                .joinedAt(LocalDateTime.now())
                .build();

        List<ConversationParticipant> conversationParticipants = participantRepository.saveAll(List.of(participant1, participant2));

        savedConv.setParticipants(conversationParticipants);

        return ConversationMapper.toConversationResponse(savedConv, currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getConversationMessages(Long conversationId, Pageable pageable) {
        UserEntity currentUser = userService.getCurrentUser();

        boolean isParticipant = participantRepository.isUserInConversation(conversationId, currentUser.getId()) == 1;

        if (!isParticipant) {
            throw new UnauthorizedException("Unauthorized access to conversation messages");
        }

        return messageRepository.findMessagesByConversationId(conversationId, pageable).map(MessageMapper::toChatMessageResponse);
    }

    @Transactional
    @Override
    public void markAsRead(Long conversationId) {
        UserEntity currentUser = userService.getCurrentUser();
        ConversationParticipant participant = participantRepository.findByConversationIdAndUserId(conversationId, currentUser.getId());

        if (participant != null) {
            participant.setLastReadAt(LocalDateTime.now());
            participantRepository.save(participant);
        }
    }
}
