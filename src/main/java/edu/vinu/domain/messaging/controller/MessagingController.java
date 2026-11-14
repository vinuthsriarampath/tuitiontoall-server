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

package edu.vinu.domain.messaging.controller;

import edu.vinu.domain.messaging.dtos.request.DeleteMessageRequest;
import edu.vinu.domain.messaging.dtos.request.EditMessageRequest;
import edu.vinu.domain.messaging.dtos.request.SendMessageRequest;
import edu.vinu.domain.messaging.dtos.request.UpdateDeliveryStatusRequest;
import edu.vinu.domain.messaging.dtos.response.ChatMessageResponse;
import edu.vinu.domain.messaging.entity.ConversationParticipant;
import edu.vinu.domain.messaging.repository.ConversationParticipantRepository;
import edu.vinu.domain.messaging.services.MessagingService;
import edu.vinu.domain.notification.dtos.requests.NotificationCreateRequest;
import edu.vinu.domain.notification.enums.NotificationType;
import edu.vinu.domain.notification.service.NotificationService;
import edu.vinu.infastructure.service.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessagingController {
    private final MessagingService messagingService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Clients send messages to: /app/chat.send
     */
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest request, Principal principal) {
        if (principal == null) {
            return; // Reject if unauthenticated
        }

        // 1. Save message to database & update conversation timestamp
        ChatMessageResponse response = messagingService.saveAndProcessMessage(principal.getName(), request);

        // 2. Broadcast the message in real-time to all subscribers of this conversation room topic
        String destination = "/topic/chat/" + request.conversationId();

        simpMessagingTemplate.convertAndSend(destination, response);
    }

    /**
     * Clients send status updates to: /app/chat.status
     * Broadcasts status changes (e.g., 'DELIVERED', 'READ')
     */
    @MessageMapping("/chat.status")
    public void updateMessageStatus(@Payload UpdateDeliveryStatusRequest request, Principal principal) {
        if (principal == null) return;

        // 1. Update status in database
        messagingService.updateMessagesStatus(request.conversationId(), request.messageIds(), request.status());

        // 2. Broadcast status update event
        String destination = "/topic/chat/" + request.conversationId() + "/status";
        simpMessagingTemplate.convertAndSend(destination, request);
    }


    /**
     * Clients send delete requests to: /app/chat.delete
     * Broadcasts message deletion events to all subscribers of the conversation room topic.
     */
    @MessageMapping("/chat.delete")
    public void deleteMessage(@Payload DeleteMessageRequest request, Principal principal) {
        if (principal == null) return;

        // 1. Soft delete message in DB and verify ownership
        ChatMessageResponse response = messagingService.deleteMessage(principal.getName(), request.messageId());

        // 2. Broadcast deletion event to the active chat room topic
        String destination = "/topic/chat/" + request.conversationId() + "/delete";
        simpMessagingTemplate.convertAndSend(destination, response);
    }

    /**
     * Clients send edit requests to: /app/chat.edit
     * Broadcasts message edit events to all subscribers of the conversation room topic.
     */
    @MessageMapping("/chat.edit")
    public void editMessage(@Payload EditMessageRequest request, Principal principal) {
        if (principal == null) return;

        ChatMessageResponse response = messagingService.editMessage(principal.getName(), request);

        String destination = "/topic/chat/" + request.conversationId() + "/edit";
        simpMessagingTemplate.convertAndSend(destination, response);
    }

}
