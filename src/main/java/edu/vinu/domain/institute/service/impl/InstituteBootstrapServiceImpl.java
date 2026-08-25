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

import edu.vinu.common.enums.ChangeValueType;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.batch.service.BatchStatService;
import edu.vinu.domain.course.enums.CourseStatus;
import edu.vinu.domain.course.service.CourseStatsService;
import edu.vinu.domain.institute.enums.InstituteTeacherStatus;
import edu.vinu.domain.institute.service.InstituteTeacherStatService;
import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.payment.service.PaymentStatService;
import edu.vinu.domain.reporting.enums.ReportingPeriod;
import edu.vinu.domain.reporting.response.ReportingPeriodRange;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.DashboardKpi;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.institute.response.InstituteBootstrapResponse;
import edu.vinu.domain.institute.response.InstituteDashboardKpiStats;
import edu.vinu.domain.institute.service.InstituteBootstrapService;
import edu.vinu.domain.institute.service.InstituteService;
import edu.vinu.domain.reporting.response.TrendPoint;
import edu.vinu.domain.reporting.service.PeriodService;
import edu.vinu.domain.reporting.utility.DifferenceCalculator;
import edu.vinu.domain.student_batch_enrollment.dto.respose.OverallEnrollmentResponse;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstituteBootstrapServiceImpl implements InstituteBootstrapService {
    private final InstituteService instituteService;
    private final InstituteTeacherStatService instituteTeacherStatService;
    private final EnrollmentService enrollmentService;
    private final PaymentStatService paymentStatService;
    private final CourseStatsService courseStatsService;
    private final BatchStatService batchStatService;
    private final PeriodService periodService;

    @Override
    public ApiResponse getCurrentInstituteBootstrapData() {
        InstituteEntity currentInstitute = instituteService.getCurrentInstitute();

        InstituteBootstrapResponse response = new InstituteBootstrapResponse();

        InstituteDashboardKpiStats kpiStats = new InstituteDashboardKpiStats();

        kpiStats.setActiveStudents(buildStudentKpi(currentInstitute.getId()));
        kpiStats.setPublishedCourses(buildCourseKpi(currentInstitute.getId()));
        kpiStats.setOngoingBatches(buildBatchKpi(currentInstitute.getId()));
        kpiStats.setActiveTeachers(buildTeacherKpi(currentInstitute.getId()));
        kpiStats.setRevenue(buildRevenueKpi(currentInstitute.getId()));

        response.setKpiStats(kpiStats);
        response.setCoursePerformance(courseStatsService.getTopPerformingCoursesByRatingAndInstituteId(currentInstitute.getId(), 5));
        response.setEnrollmentDistribution(enrollmentService.getEnrollmentDistributionByInstitute(currentInstitute.getId()));
        response.setOverallEnrollment(enrollmentService.getOverallEnrollmentStatsByInstitute(currentInstitute.getId(),currentInstitute.getUser().getCreationTimeStamp().toLocalDate()));

        return ApiResponse.builder()
                .message("Bootstrap data retrieved successfully")
                .data(response)
                .build();
    }

    private DashboardKpi buildStudentKpi(Long instituteId){

        ReportingPeriodRange periodRange = periodService.getRange(ReportingPeriod.CURRENT_MONTH);

        BigDecimal totalActiveStudents = enrollmentService.countActiveStudentsByInstitute(instituteId);

        BigDecimal currentPeriodEnrollmentCount = enrollmentService.countUniqueStudentsEnrolledBetween(instituteId, periodRange.currentStart(), periodRange.currentEnd());
        BigDecimal previousPeriodEnrollmentCount = enrollmentService.countUniqueStudentsEnrolledBetween(instituteId, periodRange.previousStart(), periodRange.previousEnd());

        BigDecimal changeValue = DifferenceCalculator.calculate(currentPeriodEnrollmentCount, previousPeriodEnrollmentCount, ChangeValueType.PERCENTAGE);

        List<TrendPoint> studentEnrollmentTrend = enrollmentService.getStudentEnrollmentTrend(instituteId, ReportingPeriod.CURRENT_MONTH, periodRange);

        return DashboardKpi.builder()
                .value(totalActiveStudents)
                .changeValue(changeValue)
                .changeValueType(ChangeValueType.PERCENTAGE)
                .changeLabel("vs Previous Month")
                .trend(studentEnrollmentTrend)
                .build();
    }

    private DashboardKpi buildCourseKpi(Long instituteId){
        ReportingPeriodRange periodRange = periodService.getRange(ReportingPeriod.CURRENT_MONTH);

        BigDecimal totalPublishedCourses = courseStatsService.countCoursesByStatusAndInstituteId(instituteId, CourseStatus.PUBLISHED);

        BigDecimal currentPeriodPublishedCoursesCount = courseStatsService.countCoursesByStatusAndInstituteIdBetween(instituteId, CourseStatus.PUBLISHED, periodRange.currentStart(), periodRange.currentEnd());
        BigDecimal previousPeriodPublishedCoursesCount = courseStatsService.countCoursesByStatusAndInstituteIdBetween(instituteId, CourseStatus.PUBLISHED, periodRange.previousStart(), periodRange.previousEnd());

        BigDecimal changeValue = DifferenceCalculator.calculate(currentPeriodPublishedCoursesCount, previousPeriodPublishedCoursesCount, ChangeValueType.ABSOLUTE);

        List<TrendPoint> coursePublishedTrend = courseStatsService.getCoursesTrendByInstituteAndStatus(instituteId, CourseStatus.PUBLISHED, ReportingPeriod.CURRENT_MONTH, periodRange);

        return DashboardKpi.builder()
                .value(totalPublishedCourses)
                .changeValue(changeValue)
                .changeValueType(ChangeValueType.ABSOLUTE)
                .changeLabel("vs Previous Month")
                .trend(coursePublishedTrend)
                .build();
    }

    private DashboardKpi buildBatchKpi(Long instituteId){
        ReportingPeriodRange periodRange = periodService.getRange(ReportingPeriod.CURRENT_MONTH);

        BigDecimal totalBatches = batchStatService.countBatchesByInstituteAndBatchStatus(instituteId, BatchStatus.ONGOING);

        BigDecimal currentPeriodBatchesCount = batchStatService.countBatchesByInstituteAndBatchStatusBetween(instituteId, BatchStatus.ONGOING, periodRange.currentStart(), periodRange.currentEnd());
        BigDecimal previousPeriodBatchesCount = batchStatService.countBatchesByInstituteAndBatchStatusBetween(instituteId, BatchStatus.ONGOING, periodRange.previousStart(), periodRange.previousEnd());

        BigDecimal changeValue = DifferenceCalculator.calculate(currentPeriodBatchesCount, previousPeriodBatchesCount, ChangeValueType.ABSOLUTE);

        List<TrendPoint> batchTrend = batchStatService.getBatchesTrendByInstituteAndBatchStatus(instituteId, BatchStatus.ONGOING, ReportingPeriod.CURRENT_MONTH, periodRange);

        return DashboardKpi.builder()
                .value(totalBatches)
                .changeValue(changeValue)
                .changeValueType(ChangeValueType.ABSOLUTE)
                .changeLabel("vs Previous Month")
                .trend(batchTrend)
                .build();
    }

    private DashboardKpi buildTeacherKpi(Long instituteId){
        ReportingPeriodRange periodRange = periodService.getRange(ReportingPeriod.CURRENT_MONTH);

        BigDecimal totalTeachers = instituteTeacherStatService.countInstituteTeachersByInstituteAndStatus(instituteId, InstituteTeacherStatus.ACTIVE);

        BigDecimal currentPeriodTeachersCount = instituteTeacherStatService.countActiveTeachersByInstituteAndStatusIdBetween(instituteId, InstituteTeacherStatus.ACTIVE, periodRange.currentStart(), periodRange.currentEnd());
        BigDecimal previousPeriodTeachersCount = instituteTeacherStatService.countActiveTeachersByInstituteAndStatusIdBetween(instituteId, InstituteTeacherStatus.ACTIVE, periodRange.previousStart(), periodRange.previousEnd());

        BigDecimal changeValue = DifferenceCalculator.calculate(currentPeriodTeachersCount, previousPeriodTeachersCount, ChangeValueType.ABSOLUTE);

        List<TrendPoint> teacherTrend = instituteTeacherStatService.getInstituteTeachersTrends(instituteId, InstituteTeacherStatus.ACTIVE, ReportingPeriod.CURRENT_MONTH, periodRange);

        return DashboardKpi.builder()
                .value(totalTeachers)
                .changeValue(changeValue)
                .changeValueType(ChangeValueType.ABSOLUTE)
                .changeLabel("vs Previous Month")
                .trend(teacherTrend)
                .build();
    }

    private DashboardKpi buildRevenueKpi(Long instituteId){
        ReportingPeriodRange periodRange = periodService.getRange(ReportingPeriod.CURRENT_MONTH);

        BigDecimal totalRevenue = paymentStatService.sumPaymentsByInstituteIdAndPaymentStatus(instituteId, PaymentStatus.PAID);

        BigDecimal currentPeriodRevenue = paymentStatService.sumPaymentsByInstituteIdAndPaymentStatusBetween(instituteId, PaymentStatus.PAID, periodRange.currentStart(), periodRange.currentEnd());
        BigDecimal previousPeriodRevenue = paymentStatService.sumPaymentsByInstituteIdAndPaymentStatusBetween(instituteId, PaymentStatus.PAID, periodRange.previousStart(), periodRange.previousEnd());

        BigDecimal changeValue = DifferenceCalculator.calculate(currentPeriodRevenue, previousPeriodRevenue, ChangeValueType.PERCENTAGE);

        List<TrendPoint> revenueTrend = paymentStatService.getRevenueTrendByInstituteAndPaymentStatus(instituteId, PaymentStatus.PAID, ReportingPeriod.CURRENT_MONTH, periodRange);

        return DashboardKpi.builder()
                .value(totalRevenue)
                .changeValue(changeValue)
                .changeValueType(ChangeValueType.PERCENTAGE)
                .changeLabel("vs Previous Month")
                .trend(revenueTrend)
                .build();
    }

}
