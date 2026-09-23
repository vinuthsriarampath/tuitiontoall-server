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

package edu.vinu.domain.student.dto.response;

import edu.vinu.domain.assignment.response.UpcomingAssignmentResponse;
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
public class StudentBootstrapResponse {
    private StudentDashboardStats dashboardStats;
    @Builder.Default
    private List<UpcomingAssignmentResponse> upcomingAssignments = new ArrayList<>();
    @Builder.Default
    private List<RecentEnrollmentResponse> recentEnrollments = new ArrayList<>();
    @Builder.Default
    private List<RecentResultsResponse> recentResults = new ArrayList<>();
}
