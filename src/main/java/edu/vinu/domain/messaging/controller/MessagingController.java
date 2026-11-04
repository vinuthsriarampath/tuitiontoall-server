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

import edu.vinu.domain.messaging.dtos.request.SendMessageRequest;
import edu.vinu.domain.messaging.dtos.response.ChatMessageResponse;
import edu.vinu.domain.messaging.services.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

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

        String senderEmail = principal.getName();

        // 1. Save message to database & update conversation timestamp
        ChatMessageResponse response = messagingService.saveAndProcessMessage(senderEmail, request);

        // 2. Broadcast the message in real-time to all subscribers of this conversation room topic
        String destination = "/topic/chat/" + request.conversationId();
        simpMessagingTemplate.convertAndSend(destination, response);
    }

}
