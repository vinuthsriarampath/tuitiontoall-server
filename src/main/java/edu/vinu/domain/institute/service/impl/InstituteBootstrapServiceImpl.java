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
import edu.vinu.domain.reporting.enums.ReportingPeriod;
import edu.vinu.domain.reporting.response.ReportingPeriodRange;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.DashboardKpi;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.institute.response.InstituteBootstrapResponse;
import edu.vinu.domain.institute.response.InstituteDashboardKpiStats;
import edu.vinu.domain.institute.service.InstituteBootstrapService;
import edu.vinu.domain.institute.service.InstituteService;
import edu.vinu.domain.institute.service.InstituteTeacherService;
import edu.vinu.domain.payment.service.PaymentService;
import edu.vinu.domain.reporting.response.TrendPoint;
import edu.vinu.domain.reporting.service.PeriodService;
import edu.vinu.domain.reporting.utility.DifferenceCalculator;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstituteBootstrapServiceImpl implements InstituteBootstrapService {
    private final InstituteService instituteService;
    private final InstituteTeacherService instituteTeacherService;
    private final EnrollmentService enrollmentService;
    private final PaymentService paymentService;
    private final CourseService courseService;
    private final PeriodService periodService;

    @Override
    public ApiResponse getCurrentInstituteBootstrapData() {
        InstituteEntity currentInstitute = instituteService.getCurrentInstitute();

        InstituteBootstrapResponse response = new InstituteBootstrapResponse();

        InstituteDashboardKpiStats kpiStats = new InstituteDashboardKpiStats();

        kpiStats.setActiveStudents(buildStudentKpi(currentInstitute.getId()));

        response.setKpiStats(kpiStats);

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
}
