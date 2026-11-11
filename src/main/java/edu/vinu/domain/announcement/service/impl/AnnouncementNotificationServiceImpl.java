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

package edu.vinu.domain.announcement.service.impl;

import edu.vinu.domain.announcement.entity.AnnouncementEntity;
import edu.vinu.domain.announcement.enums.AnnouncementStatus;
import edu.vinu.domain.announcement.service.AnnouncementNotificationService;
import edu.vinu.domain.batch.service.BatchService;
import edu.vinu.domain.course.service.TeacherCourseService;
import edu.vinu.domain.institute.service.InstituteTeacherService;
import edu.vinu.domain.notification.dtos.requests.NotificationCreateRequest;
import edu.vinu.domain.notification.enums.NotificationType;
import edu.vinu.domain.notification.service.NotificationService;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementNotificationServiceImpl implements AnnouncementNotificationService {

    private final NotificationService notificationService;
    private final BatchService batchService;
    private final EnrollmentService enrollmentService;
    private final TeacherCourseService teacherCourseService;
    private final InstituteTeacherService instituteTeacherService;

    @Async
    @Override
    public void notifyUsers(AnnouncementEntity announcement, Long courseId, Long batchId) {
        if (!AnnouncementStatus.PUBLISHED.equals(announcement.getStatus())) {
            return;
        }

        Set<Long> recipientUserIds = new HashSet<>();

        switch (announcement.getVisibility()) {
            case BATCH -> {
                if (batchId != null) {
                    recipientUserIds.addAll(batchService.getUserIdsOfTeachersAssignedToBatch(batchId));
                    recipientUserIds.addAll(enrollmentService.getUserIdsEnrolledInBatch(batchId));
                }
                if (recipientUserIds.isEmpty()) {
                    return;
                }
                String batchName = (announcement.getBatch() != null) ? announcement.getBatch().getName() : "Batch";

                recipientUserIds.forEach(userId -> notificationService.createNotification(
                        NotificationCreateRequest.builder()
                                .userId(userId)
                                .title("Announcement for: " + batchName)
                                .content(announcement.getTitle())
                                .type(NotificationType.ANNOUNCEMENT)
                                .entityType("ANNOUNCEMENT")
                                .entityId(announcement.getId())
                                .targetRoute("/stu/my-learnings/courses/" + courseId + "/batches/" + batchId + "/announcements/" + announcement.getId())
                                .build()
                ));
            }
            case COURSE -> {
                if (courseId != null) {
                    recipientUserIds.addAll(teacherCourseService.getUserIdsOfTeachersAssignedToCourse(courseId));
                    recipientUserIds.addAll(enrollmentService.getUserIdsEnrolledInCourse(courseId));
                }
                if (recipientUserIds.isEmpty()) {
                    return;
                }
                String courseTitle = (announcement.getCourse() != null) ? announcement.getCourse().getTitle() : "Course";

                recipientUserIds.forEach(userId -> notificationService.createNotification(
                        NotificationCreateRequest.builder()
                                .userId(userId)
                                .title("Announcement for: " + courseTitle)
                                .content(announcement.getTitle())
                                .type(NotificationType.ANNOUNCEMENT)
                                .entityType("ANNOUNCEMENT")
                                .entityId(announcement.getId())
                                .targetRoute("/stu/my-learnings/courses/" + courseId + "/announcements/" + announcement.getId())
                                .build()
                ));
            }
            case ALL_TEACHERS -> {
                if (announcement.getInstitute() != null) {
                    List<Long> teachers = instituteTeacherService.getUserIdsWorksInInstitute(announcement.getInstitute().getId());

                    if (teachers == null || teachers.isEmpty()) {
                        return;
                    }

                    String instituteName = announcement.getInstitute().getInstituteName();

                    teachers.forEach(userId -> notificationService.createNotification(
                            NotificationCreateRequest.builder()
                                    .userId(userId)
                                    .title("Announcement for: " + instituteName)
                                    .content(announcement.getTitle())
                                    .type(NotificationType.ANNOUNCEMENT)
                                    .entityType("ANNOUNCEMENT")
                                    .entityId(announcement.getId())
                                    .targetRoute("/announcements/" + announcement.getId())
                                    .build()
                    ));
                }
            }
        }
        log.info("Successfully dispatched notifications for announcement ID: {}", announcement.getId());
    }
}
