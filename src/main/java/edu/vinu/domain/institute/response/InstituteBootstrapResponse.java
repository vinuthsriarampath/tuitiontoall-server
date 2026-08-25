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

package edu.vinu.domain.institute.response;

import edu.vinu.domain.course.response.CoursePerformanceResponse;
import edu.vinu.domain.student_batch_enrollment.dto.respose.EnrollmentDistributionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstituteBootstrapResponse{
    @Builder.Default
    private InstituteDashboardKpiStats kpiStats = new InstituteDashboardKpiStats();
    @Builder.Default
    private List<CoursePerformanceResponse> coursePerformance = new ArrayList<>();
    @Builder.Default
    private EnrollmentDistributionResponse enrollmentDistribution = new EnrollmentDistributionResponse();
}
