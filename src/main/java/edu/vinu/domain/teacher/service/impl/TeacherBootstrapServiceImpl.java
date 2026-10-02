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

package edu.vinu.domain.teacher.service.impl;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.schedule_lecture.service.ScheduleLectureService;
import edu.vinu.domain.student_assignment_submit.service.AssignmentSubmitService;
import edu.vinu.domain.teacher.dtos.response.TeacherBootstrapResponse;
import edu.vinu.domain.teacher.dtos.response.TeacherDashboardStats;
import edu.vinu.domain.teacher.entity.TeacherEntity;
import edu.vinu.domain.teacher.service.TeacherBootstrapService;
import edu.vinu.domain.teacher.service.TeacherService;
import edu.vinu.domain.teacher.service.TeacherStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherBootstrapServiceImpl implements TeacherBootstrapService {

    private final TeacherService teacherService;
    private final TeacherStatsService teacherStatsService;
    private final CourseService courseService;
    private final ScheduleLectureService scheduleLectureService;
    private final AssignmentSubmitService submissionService;

    @Override
    public ApiResponse getTeacherDashboardStats() {

        TeacherEntity currentTeacher = teacherService.getCurrentTeacher();

        TeacherBootstrapResponse response = new TeacherBootstrapResponse();
        response.setStats(this.buildTeacherDashboardStats(currentTeacher.getId()));
        response.setAssignedPublishedCourses(courseService.getPublishedAssignedCoursesByTeacher(currentTeacher.getId()));
        response.setUpcomingLectureSchedules(scheduleLectureService.getUpcomingLecturesForTeacher(currentTeacher.getId()));
        response.setNonGradedSubmissions(submissionService.getAllNonGradedSubmissionsForTeacher(currentTeacher.getId()));

        return ApiResponse.builder()
                .message("Teacher dashboard stats retrieved successfully")
                .data(response)
                .build();
    }

    private TeacherDashboardStats buildTeacherDashboardStats(Long teacherId) {
        return TeacherDashboardStats.builder()
                .ongoingAssignedBatchesCount(teacherStatsService.getAssignedBatchesCount(teacherId, BatchStatus.ONGOING))
                .publishedAssignedModulesCount(teacherStatsService.getAssignedModulesCount(teacherId, BatchStatus.ONGOING))
                .pendingEvaluationsCount(teacherStatsService.getPendingEvaluationCount(teacherId))
                .build();
    }
}
