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

package edu.vinu.domain.course.service.impl;

import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.batch.repository.BatchRepository;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.course.repository.CourseRepository;
import edu.vinu.domain.course.response.CourseStatsResponse;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.course.service.CourseStatsService;
import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.payment.repository.PaymentRepository;
import edu.vinu.domain.student_batch_enrollment.enums.StudentBatchEnrollmentStatus;
import edu.vinu.domain.student_batch_enrollment.repository.StudentBatchEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CourseStatsServiceImpl implements CourseStatsService {
    private final CourseService courseService;
    private final BatchRepository batchRepository;
    private final StudentBatchEnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public ApiResponse getCourseStats(Long courseId) {

        CourseEntity courseEntity = courseService.getCourseEntityById(courseId);

        CourseStatsResponse stats = new CourseStatsResponse();
        stats.setCourseId(courseId);

        if(courseService.isCourseOwner(courseEntity)) {
            Long ongoingBatchCount = batchRepository.countByCourseIdAndBatchStatus(courseId, BatchStatus.ONGOING.toString());
            Long activeStudentsCount = enrollmentRepository.countActiveStudentsByCourseId(courseId, BatchStatus.ONGOING.toString(), StudentBatchEnrollmentStatus.ACTIVE.toString());
            BigDecimal totalRevenue = paymentRepository.sumRevenueByCourseId(courseId, PaymentStatus.PAID);

            stats.setOngoingBatches(ongoingBatchCount);
            stats.setActiveStudents(activeStudentsCount);
            stats.setTotalRevenue(totalRevenue);
        }else {
            throw new UnauthorizedException("User is not authorized to view stats for this course");
        }

        return ApiResponse.builder()
                .message("Course stats retrieved successfully")
                .data(stats)
                .build();
    }
}
