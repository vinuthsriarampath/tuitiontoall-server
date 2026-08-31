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

package edu.vinu.domain.institute.service;

import edu.vinu.common.response.DashboardKpi;

public interface InstituteDashboardKpiService {
    DashboardKpi buildStudentKpi(Long instituteId);

    DashboardKpi buildCourseKpi(Long instituteId);

    DashboardKpi buildBatchKpi(Long instituteId);

    DashboardKpi buildTeacherKpi(Long instituteId);

    DashboardKpi buildRevenueKpi(Long instituteId);
}
