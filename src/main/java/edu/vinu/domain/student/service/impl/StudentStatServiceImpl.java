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

package edu.vinu.domain.student.service.impl;

import edu.vinu.common.response.DashboardStats;
import edu.vinu.domain.assignment.service.AssignmentStatService;
import edu.vinu.domain.grading.service.GradingService;
import edu.vinu.domain.student.service.StudentStatService;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StudentStatServiceImpl implements StudentStatService {
    private final EnrollmentStatService enrollmentStatService;
    private final AssignmentStatService assignmentStatService;
    private final GradingService gradingService;
    @Override
    public DashboardStats getTotalEnrollmentsStats(Long studentId) {
        return DashboardStats.builder()
                .value(BigDecimal.valueOf(enrollmentStatService.getStudentCourseEnrollmentCount(studentId)))
                .label("Total Enrollments")
                .build();
    }

    @Override
    public DashboardStats getPendingAssignmentsStats(Long studentId) {
        return DashboardStats.builder()
                .value(BigDecimal.valueOf(assignmentStatService.getStudentPendingAssignmentCount(studentId)))
                .label("Pending Assignments")
                .build();
    }

    @Override
    public DashboardStats getCompletedAssignmentsStats(Long studentId) {
        return DashboardStats.builder()
                .value(BigDecimal.valueOf(assignmentStatService.getStudentCompletedAssignmentCount(studentId)))
                .label("Completed Assignments")
                .build();
    }

    @Override
    public DashboardStats getAverageMarksStats(Long studentId) {
        return DashboardStats.builder()
                .value(gradingService.getAverageMarksOfStudent(studentId))
                .label("Average Marks")
                .build();
    }
}
