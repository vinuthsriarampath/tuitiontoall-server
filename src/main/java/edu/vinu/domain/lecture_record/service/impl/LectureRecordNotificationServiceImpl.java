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

package edu.vinu.domain.lecture_record.service.impl;

import edu.vinu.domain.chapter.response.ChapterHierarchyResponse;
import edu.vinu.domain.chapter.service.ChapterService;
import edu.vinu.domain.lecture_record.entity.LectureRecordEntity;
import edu.vinu.domain.lecture_record.service.LectureRecordNotificationService;
import edu.vinu.domain.notification.dtos.requests.NotificationCreateRequest;
import edu.vinu.domain.notification.enums.NotificationType;
import edu.vinu.domain.notification.service.NotificationService;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureRecordNotificationServiceImpl implements LectureRecordNotificationService {

    private final NotificationService notificationService;
    private final ChapterService chapterService;
    private final EnrollmentService enrollmentService;

    @Async
    @Override
    public void notifyUsers(LectureRecordEntity lectureRecord, Long chapterId) {

        ChapterHierarchyResponse hierarchy = chapterService.getChapterHierarchy(chapterId);

        List<Long> userIds = enrollmentService.getUserIdsEnrolledInBatch(hierarchy.batchId());

        String targetRoute = String.format("/stu/my-learnings/courses/%d/batches/%d/modules/%d/chapters/%d/watch?f=%s", hierarchy.courseId(), hierarchy.batchId(), hierarchy.moduleId(), hierarchy.chapterId(), lectureRecord.getUrl());

        userIds.forEach(userId -> notificationService.createNotification(
                NotificationCreateRequest.builder()
                        .userId(userId)
                        .title("New Lecture Record!")
                        .content(lectureRecord.getTitle())
                        .type(NotificationType.LECTURE)
                        .entityType("LECTURE_RECORD")
                        .entityId(lectureRecord.getId())
                        .targetRoute(targetRoute)
                        .build()
                )
        );

        log.atInfo().log("Notifications sent for lecture record {} to users: {}", lectureRecord.getId(), userIds);
    }
}
