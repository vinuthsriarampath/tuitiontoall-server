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

package edu.vinu.domain.notification.dtos.responses;

import edu.vinu.domain.notification.enums.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        Long id,
        Long userId,
        String title,
        String content,
        NotificationType type,
        String entityType,
        Long entityId,
        String targetRoute,
        Boolean isRead,
        LocalDateTime readAt,
        LocalDateTime createdAt
) {
}
