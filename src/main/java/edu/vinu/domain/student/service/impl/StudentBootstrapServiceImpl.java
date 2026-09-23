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

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.assignment.service.AssignmentService;
import edu.vinu.domain.grading.service.GradingService;
import edu.vinu.domain.student.dto.response.StudentBootstrapResponse;
import edu.vinu.domain.student.dto.response.StudentDashboardStats;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.student.service.StudentBootstrapService;
import edu.vinu.domain.student.service.StudentService;
import edu.vinu.domain.student.service.StudentStatService;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentBootstrapServiceImpl implements StudentBootstrapService {
    private final StudentService studentService;
    private final StudentStatService studentStatService;
    private final AssignmentService assignmentService;
    private final EnrollmentService enrollmentService;
    private final GradingService gradingService;

    @Override
    public ApiResponse getBootstrapData() {
        StudentEntity currentStudent = studentService.getCurrentStudent();

        StudentBootstrapResponse response = new StudentBootstrapResponse();

        response.setDashboardStats(getStudentDashboardStats(currentStudent.getId()));
        response.setUpcomingAssignments(assignmentService.getTop5UpcomingAssignmentsSubmissionsForStudent(currentStudent.getId()));
        response.setRecentEnrollments(enrollmentService.getRecent5EnrollmentsByStudent(currentStudent.getId()));
        response.setRecentResults(gradingService.getRecent5Results(currentStudent.getId()));

        return ApiResponse.builder()
                .message("Bootstrap data retrieved successfully")
                .data(response)
                .build();
    }

    private StudentDashboardStats getStudentDashboardStats(Long studentId) {
        StudentDashboardStats dashboardStats = new StudentDashboardStats();

        dashboardStats.setEnrolledCourses(studentStatService.getTotalEnrollmentsStats(studentId));
        dashboardStats.setPendingAssignments(studentStatService.getPendingAssignmentsStats(studentId));
        dashboardStats.setCompletedAssignments(studentStatService.getCompletedAssignmentsStats(studentId));
        dashboardStats.setAverageMarks(studentStatService.getAverageMarksStats(studentId));
        return dashboardStats;
    }
}
