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

package edu.vinu.domain.notification.mapper;

import edu.vinu.domain.notification.dtos.requests.NotificationCreateRequest;
import edu.vinu.domain.notification.dtos.responses.NotificationResponse;
import edu.vinu.domain.notification.entity.Notification;
import edu.vinu.domain.user.entity.UserEntity;

public class NotificationMapper {
    public static Notification toNotificationEntity(NotificationCreateRequest request, UserEntity user) {
        if (request == null) {
            return null;
        }
        return Notification.builder()
                .user(user)
                .title(request.title())
                .content(request.content())
                .type(request.type())
                .entityType(request.entityType())
                .entityId(request.entityId())
                .targetRoute(request.targetRoute())
                .isRead(false)
                .build();
    }

    public static NotificationResponse toNotificationResponse(Notification notification) {
        if (notification == null) {
            return null;
        }
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser() != null ? notification.getUser().getId() : null)
                .title(notification.getTitle())
                .content(notification.getContent())
                .type(notification.getType())
                .entityType(notification.getEntityType())
                .entityId(notification.getEntityId())
                .targetRoute(notification.getTargetRoute())
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
