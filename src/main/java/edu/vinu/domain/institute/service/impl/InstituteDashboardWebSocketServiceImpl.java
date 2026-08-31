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

package edu.vinu.domain.institute.service.impl;

import edu.vinu.domain.batch.response.BatchMetricsUpdatedResponse;
import edu.vinu.domain.course.response.CourseMetricsUpdatedResponse;
import edu.vinu.domain.institute.response.InstituteTeacherMatricsUpdatedResponse;
import edu.vinu.domain.institute.service.InstituteDashboardWebSocketService;
import edu.vinu.domain.student_batch_enrollment.dto.respose.EnrollmentMetricsUpdatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstituteDashboardWebSocketServiceImpl implements InstituteDashboardWebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void updateEnrollmentMetrics(Long instituteId, EnrollmentMetricsUpdatedResponse response) {
        String destination = "/topic/institute/" + instituteId + "/enrollment-metrics";
        simpMessagingTemplate.convertAndSend(destination, response);
    }

    @Override
    public void updateTeacherMetrics(Long instituteId, InstituteTeacherMatricsUpdatedResponse response) {
        String destination = "/topic/institute/"+ instituteId + "/active-teacher-metrics";
        simpMessagingTemplate.convertAndSend(destination, response);
    }

    @Override
    public void updateCourseMetrics(Long instituteId, CourseMetricsUpdatedResponse build) {
        String destination = "/topic/institute/"+ instituteId + "/course-metrics";
        simpMessagingTemplate.convertAndSend(destination, build);
    }

    @Override
    public void updateBatchMetrics(Long instituteId, BatchMetricsUpdatedResponse build) {
        String destination = "/topic/institute/"+ instituteId + "/batch-metrics";
        simpMessagingTemplate.convertAndSend(destination, build);
    }
}
