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

package edu.vinu.domain.notification.service.impl;

import edu.vinu.domain.notification.dtos.responses.NotificationResponse;
import edu.vinu.domain.notification.entity.Notification;
import edu.vinu.domain.notification.mapper.NotificationMapper;
import edu.vinu.domain.notification.service.NotificationWebSocketService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationWebSocketServiceImpl implements NotificationWebSocketService {

    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotificationToUser(Long userId, Notification notification) {
        UserEntity user = userService.getUserEntityById(userId);
        NotificationResponse response = NotificationMapper.toNotificationResponse(notification);

        // Spring Boot convertAndSendToUser targets /user/{email}/queue/notifications
        messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", response);
    }
}
