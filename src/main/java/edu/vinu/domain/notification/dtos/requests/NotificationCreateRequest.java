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

package edu.vinu.domain.notification.dtos.requests;

import edu.vinu.domain.notification.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationCreateRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Content is required")
        String content,

        @NotNull(message = "Notification type is required")
        NotificationType type,

        String entityType,
        Long entityId,
        String targetRoute
) {
}
