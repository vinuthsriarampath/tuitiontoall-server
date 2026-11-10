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

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.notification.dtos.requests.NotificationCreateRequest;
import edu.vinu.domain.notification.dtos.responses.NotificationResponse;
import edu.vinu.domain.notification.entity.Notification;
import edu.vinu.domain.notification.mapper.NotificationMapper;
import edu.vinu.domain.notification.repository.NotificationRepository;
import edu.vinu.domain.notification.service.NotificationService;
import edu.vinu.domain.notification.service.NotificationWebSocketService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserService userService;
    private final NotificationRepository notificationRepository;
    private final NotificationWebSocketService notificationWebSocketService;

    @Override
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        UserEntity user = userService.getCurrentUser();

        Notification notification = NotificationMapper.toNotificationEntity(request, user);
        Notification savedNotification = notificationRepository.save(notification);

        notificationWebSocketService.sendNotificationToUser(request.userId(), savedNotification);

        return NotificationMapper.toNotificationResponse(savedNotification);
    }

    @Override
    public Page<NotificationResponse> getUserNotifications(Long userId, PaginationRequest pagination) {

        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of("created_at")));

        Page<Notification> notificationPage = notificationRepository.findAllByUserId(userId, pageable);
        return notificationPage.map(NotificationMapper::toNotificationResponse);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        int updatedRows = notificationRepository.markAsRead(notificationId, userId);
        if (updatedRows == 0) {
            throw new RuntimeException("Notification not found or unauthorized for user ID: " + userId);
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }
}
