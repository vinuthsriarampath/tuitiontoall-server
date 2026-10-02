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

package edu.vinu.domain.teacher.service;

import edu.vinu.common.response.DashboardStats;
import edu.vinu.domain.batch.enums.BatchStatus;

public interface TeacherStatsService {
    DashboardStats getAssignedBatchesCount(Long teacherId);
    DashboardStats getAssignedBatchesCount(Long teacherId, BatchStatus batchStatus);
    DashboardStats getAssignedModulesCount(Long teacherId);
    DashboardStats getAssignedModulesCount(Long teacherId, BatchStatus batchStatus);
    DashboardStats getPendingEvaluationCount(Long teacherId);
}
