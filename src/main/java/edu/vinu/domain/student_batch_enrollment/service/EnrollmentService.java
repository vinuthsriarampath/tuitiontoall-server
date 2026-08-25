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

package edu.vinu.domain.student_batch_enrollment.service;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.reporting.enums.ReportingPeriod;
import edu.vinu.domain.reporting.response.ReportingPeriodRange;
import edu.vinu.domain.reporting.response.TrendPoint;
import edu.vinu.domain.student.dto.response.StudentUserResponse;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentEligibilityCheckRequest;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentRequest;
import edu.vinu.domain.student_batch_enrollment.dto.respose.EnrollmentDistributionResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface EnrollmentService {
    byte[] enrollStudent(EnrollmentRequest request);

    ApiResponse checkEnrollmentEligibility(EnrollmentEligibilityCheckRequest request);

    PaginatedApiResponse<StudentUserResponse> getStudentsByBatch(Long batchId, PaginationRequest pagination);

    BigDecimal countActiveStudentsByInstitute(Long instituteId);

    BigDecimal countUniqueStudentsEnrolledBetween(Long instituteId, LocalDateTime start, LocalDateTime end);

    List<TrendPoint> getStudentEnrollmentTrend(Long instituteId, ReportingPeriod period, ReportingPeriodRange range);

    EnrollmentDistributionResponse getEnrollmentDistributionByInstitute(Long instituteId);
}
