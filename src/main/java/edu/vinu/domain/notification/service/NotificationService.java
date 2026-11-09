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

package edu.vinu.domain.notification.service;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.domain.notification.dtos.requests.NotificationCreateRequest;
import edu.vinu.domain.notification.dtos.responses.NotificationResponse;
import org.springframework.data.domain.Page;

public interface NotificationService {
    NotificationResponse createNotification(NotificationCreateRequest request);

    Page<NotificationResponse> getUserNotifications(Long userId, PaginationRequest pagination);

    long getUnreadCount(Long userId);

    void markAsRead(Long notificationId, Long userId);

    void markAllAsRead(Long userId);
}
