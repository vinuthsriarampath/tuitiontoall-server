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

package edu.vinu.domain.messaging.websocket;

import edu.vinu.domain.messaging.dtos.request.ConversationEventResponse;
import edu.vinu.domain.messaging.enums.ConversationEventType;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConversationEventPublisher {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    private static final String DESTINATION = "/queue/chat-events";

    public void publishMessageReceived(Long recipientId, Long conversationId, Long messageId) {
        publish(recipientId, new ConversationEventResponse(ConversationEventType.MESSAGE_RECEIVED, conversationId, messageId));
    }

    public void publishConversationRead(Long recipientId, Long conversationId) {
        publish(recipientId, new ConversationEventResponse(ConversationEventType.CONVERSATION_READ, conversationId, null));
    }

    private void publish(Long recipientId, ConversationEventResponse event) {
        UserEntity recipient = userService.getUserEntityById(recipientId);

        messagingTemplate.convertAndSendToUser(recipient.getEmail(), DESTINATION, event);
    }
}
