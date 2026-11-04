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

import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.messaging.dtos.response.ChatMessageResponse;
import edu.vinu.domain.messaging.dtos.response.ConversationResponse;
import edu.vinu.domain.messaging.services.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;
import static edu.vinu.common.util.ResponseUtil.createPaginatedApiResponse;

@RestController
@RequestMapping("/api/v2/conversations")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    /**
     * Fetch all conversation threads for the authenticated user.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ConversationResponse>>> getUserConversations() {
        List<ConversationResponse> conversations = conversationService.getUserConversations();
        return ResponseEntity.ok(createApiResponse("Conversations retrieved successfully",conversations));
    }

    /**
     * Start or fetch an existing direct 1-to-1 conversation with another user.
     */
    @PostMapping("/direct/{targetUserId}")
    public ResponseEntity<ApiResponse<ConversationResponse>> getOrCreateDirectConversation(@PathVariable Long targetUserId) {
        ConversationResponse conversation = conversationService.getOrCreateDirectConversation(targetUserId);
        return ResponseEntity.ok(createApiResponse("Conversation ready",conversation));
    }

    /**
     * Fetch message history for a conversation room using pagination.
     */
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<PaginatedApiResponse<ChatMessageResponse>> getConversationMessages(@PathVariable Long conversationId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessageResponse> messages = conversationService.getConversationMessages(conversationId, pageable);
        return ResponseEntity.ok(createPaginatedApiResponse("Messages retrieved successfully", messages));
    }
}

