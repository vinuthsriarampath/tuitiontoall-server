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

package edu.vinu.domain.notification.controller;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.notification.dtos.responses.NotificationResponse;
import edu.vinu.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;
import static edu.vinu.common.util.ResponseUtil.createPaginatedApiResponse;

@RestController
@RequestMapping("/api/v2/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * Get paginated notifications for a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<PaginatedApiResponse<NotificationResponse>> getUserNotifications(@PathVariable Long userId, PaginationRequest pagination) {
        Page<NotificationResponse> notificationPage = notificationService.getUserNotifications(userId, pagination);
        return ResponseEntity.ok(createPaginatedApiResponse("Notifications fetched successfully.", notificationPage));
    }

    /**
     * Get unread notification count for badge display.
     */
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(createApiResponse("Unread count fetched successfully.", count));
    }

    /**
     * Mark a specific notification as read.
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long notificationId, @RequestParam Long userId) {
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok(createApiResponse("Notification marked as read.", null));
    }

    /**
     * Mark all unread notifications as read for a user ("Mark all as read").
     */
    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(createApiResponse("All notifications marked as read.", null));
    }
}
