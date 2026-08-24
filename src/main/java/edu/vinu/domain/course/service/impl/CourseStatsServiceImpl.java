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
import edu.vinu.domain.course.enums.CourseStatus;
import edu.vinu.domain.course.repository.CourseRepository;
import edu.vinu.domain.course.repository.SimpleCourseProjection.SimpleCourseProjection;
import edu.vinu.domain.course.response.CoursePerformanceResponse;
import edu.vinu.domain.course.response.CourseStatsResponse;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.course.service.CourseStatsService;
import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.payment.repository.PaymentRepository;
import edu.vinu.domain.reporting.enums.ReportingPeriod;
import edu.vinu.domain.reporting.enums.TrendType;
import edu.vinu.domain.reporting.projection.TrendPointProjection;
import edu.vinu.domain.reporting.response.ReportingPeriodRange;
import edu.vinu.domain.reporting.response.TrendPoint;
import edu.vinu.domain.reporting.utility.TrendBuilder;
import edu.vinu.domain.student_batch_enrollment.enums.StudentBatchEnrollmentStatus;
import edu.vinu.domain.student_batch_enrollment.repository.StudentBatchEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseStatsServiceImpl implements CourseStatsService {
    private final CourseService courseService;
    private final BatchRepository batchRepository;
    private final StudentBatchEnrollmentRepository enrollmentRepository;
    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;

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

    @Override
    public BigDecimal countCoursesByStatusAndInstituteId(Long instituteId, CourseStatus status) {
        Long count = courseRepository.countCoursesByStatusAndInstituteId(status.name(), instituteId);
        if(count == null) count = 0L;
        return BigDecimal.valueOf(count);
    }

    @Override
    public BigDecimal countCoursesByStatusAndInstituteIdBetween(Long instituteId, CourseStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Long count = courseRepository.countCoursesByStatusAndInstituteIdBetween(status.name(), instituteId, startDateTime, endDateTime);
        if(count == null) count = 0L;
        return BigDecimal.valueOf(count);
    }

    @Override
    public List<TrendPoint> getCoursesTrendByInstituteAndStatus(Long instituteId, CourseStatus status, ReportingPeriod period, ReportingPeriodRange range) {
        return switch (period){
            case TODAY -> getHourlyCourseTrend(instituteId, status, range.currentStart(), range.currentEnd());
            case CURRENT_WEEK, CURRENT_MONTH, CURRENT_3_MONTHS -> getDailyCourseTrend(instituteId, status, range.currentStart(), range.currentEnd());
            case CURRENT_YEAR, OVERALL -> getMonthlyCourseTrend(instituteId, status, range.currentStart(), range.currentEnd());
        };
    }

    @Override
    public List<CoursePerformanceResponse> getTopPerformingCoursesByRatingAndInstituteId(Long instituteId, int limit) {
        List<SimpleCourseProjection> topPerformingCourses = courseRepository.getTopPerformingCoursesByRatingAndInstituteId(instituteId, limit);
        List<CoursePerformanceResponse> responseList = new ArrayList<>();
        for (SimpleCourseProjection course : topPerformingCourses){
                Long ongoingBatchCount = batchRepository.countByCourseIdAndBatchStatus(course.getId(), BatchStatus.ONGOING.name());
                Long activeStudentsCount = enrollmentRepository.countActiveStudentsByCourseId(course.getId(), BatchStatus.ONGOING.name(), StudentBatchEnrollmentStatus.ACTIVE.name());

                responseList.add(
                        CoursePerformanceResponse.builder()
                                .id(course.getId())
                                .title(course.getTitle())
                                .activeStudentsCount(activeStudentsCount)
                                .ongoingBatchesCount(ongoingBatchCount)
                                .avgRating(course.getAvgRating())
                                .build()
                );
        }
        return responseList;
    }


    private List<TrendPoint> getHourlyCourseTrend(Long instituteId, CourseStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<TrendPointProjection> hourlyCourseTrend = courseRepository.getHourlyCoursesTrendByStatusAndInstitute(status.name(), instituteId, startDateTime, endDateTime);
        return TrendBuilder.build(startDateTime, endDateTime, TrendType.HOUR, hourlyCourseTrend);
    }

    private List<TrendPoint> getDailyCourseTrend(Long instituteId, CourseStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<TrendPointProjection> dailyCourseTrend = courseRepository.getDailyCoursesTrendByStatusAndInstitute(status.name(), instituteId, startDateTime, endDateTime);
        return TrendBuilder.build(startDateTime, endDateTime, TrendType.DAY, dailyCourseTrend);
    }

    private List<TrendPoint> getMonthlyCourseTrend(Long instituteId, CourseStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<TrendPointProjection> monthlyCourseTrend = courseRepository.getMonthlyCoursesTrendByStatusAndInstitute(status.name(), instituteId, startDateTime, endDateTime);
        return TrendBuilder.build(startDateTime, endDateTime, TrendType.MONTH, monthlyCourseTrend);
    }
}
