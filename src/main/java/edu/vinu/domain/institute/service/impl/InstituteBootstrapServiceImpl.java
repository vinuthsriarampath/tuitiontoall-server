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

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.enums.ChangeValueType;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.DashboardKpi;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.batch.request.BatchFilterRequest;
import edu.vinu.domain.batch.service.BatchService;
import edu.vinu.domain.batch.service.BatchStatService;
import edu.vinu.domain.course.enums.CourseStatus;
import edu.vinu.domain.course.service.CourseStatsService;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.institute.enums.InstituteTeacherStatus;
import edu.vinu.domain.institute.response.InstituteBootstrapResponse;
import edu.vinu.domain.institute.response.InstituteDashboardKpiStats;
import edu.vinu.domain.institute.service.InstituteBootstrapService;
import edu.vinu.domain.institute.service.InstituteDashboardKpiService;
import edu.vinu.domain.institute.service.InstituteService;
import edu.vinu.domain.institute.service.InstituteTeacherStatService;
import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.payment.service.PaymentStatService;
import edu.vinu.domain.reporting.enums.ReportingPeriod;
import edu.vinu.domain.reporting.response.ReportingPeriodRange;
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
    private final EnrollmentService enrollmentService;
    private final CourseStatsService courseStatsService;
    private final BatchService batchService;
    private final InstituteDashboardKpiService instituteDashboardKpiService;

    @Override
    public ApiResponse getCurrentInstituteBootstrapData() {
        InstituteEntity currentInstitute = instituteService.getCurrentInstitute();

        InstituteBootstrapResponse response = new InstituteBootstrapResponse();

        InstituteDashboardKpiStats kpiStats = new InstituteDashboardKpiStats();

        kpiStats.setActiveStudents(instituteDashboardKpiService.buildStudentKpi(currentInstitute.getId()));
        kpiStats.setPublishedCourses(instituteDashboardKpiService.buildCourseKpi(currentInstitute.getId()));
        kpiStats.setOngoingBatches(instituteDashboardKpiService.buildBatchKpi(currentInstitute.getId()));
        kpiStats.setActiveTeachers(instituteDashboardKpiService.buildTeacherKpi(currentInstitute.getId()));
        kpiStats.setRevenue(instituteDashboardKpiService.buildRevenueKpi(currentInstitute.getId()));

        response.setKpiStats(kpiStats);
        response.setCoursePerformance(courseStatsService.getTopPerformingCoursesByRatingAndInstituteId(currentInstitute.getId(), 5));
        response.setEnrollmentDistribution(enrollmentService.getEnrollmentDistributionByInstitute(currentInstitute.getId()));
        response.setOverallEnrollment(enrollmentService.getOverallEnrollmentStatsByInstitute(currentInstitute.getId(),currentInstitute.getUser().getCreationTimeStamp().toLocalDate()));
        response.setActiveBatches(batchService.getAllBatches(new PaginationRequest(0,5,null,  List.of("created_date")), new BatchFilterRequest(null, null, null, currentInstitute.getId(), BatchStatus.ONGOING, null)));

        return ApiResponse.builder()
                .message("Bootstrap data retrieved successfully")
                .data(response)
                .build();
    }

}
