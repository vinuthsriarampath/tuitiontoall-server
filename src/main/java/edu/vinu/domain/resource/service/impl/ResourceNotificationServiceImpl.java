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

package edu.vinu.domain.resource.service.impl;

import edu.vinu.domain.chapter.response.ChapterHierarchyResponse;
import edu.vinu.domain.chapter.service.ChapterService;
import edu.vinu.domain.notification.dtos.requests.NotificationCreateRequest;
import edu.vinu.domain.notification.enums.NotificationType;
import edu.vinu.domain.notification.service.NotificationService;
import edu.vinu.domain.resource.entity.ResourceEntity;
import edu.vinu.domain.resource.service.ResourceNotificationService;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceNotificationServiceImpl implements ResourceNotificationService {

    private final NotificationService notificationService;
    private final ChapterService chapterService;
    private final EnrollmentService enrollmentService;

    @Override
    public void notifyUsers(ResourceEntity resource, Long chapterId) {

        ChapterHierarchyResponse hierarchy = chapterService.getChapterHierarchy(chapterId);

        List<Long> userIds = enrollmentService.getUserIdsEnrolledInBatch(hierarchy.batchId());

        String targetRoute = String.format("/stu/my-learnings/courses/%d/batches/%d/modules/%d/chapters/%d", hierarchy.courseId(), hierarchy.batchId(), hierarchy.moduleId(), hierarchy.chapterId());

        userIds.forEach(userId -> {
            notificationService.createNotification(
                    NotificationCreateRequest.builder()
                            .userId(userId)
                            .title("New Resource Available!")
                            .content(resource.getName())
                            .type(NotificationType.RESOURCE)
                            .entityType("RESOURCE")
                            .entityId(resource.getId())
                            .targetRoute(targetRoute)
                            .build()
            );
        });
    }


}
