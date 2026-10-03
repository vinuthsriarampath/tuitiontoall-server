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

package edu.vinu.domain.teacher.service.impl;

import edu.vinu.common.response.DashboardStats;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.batch.service.BatchService;
import edu.vinu.domain.module.service.ModuleService;
import edu.vinu.domain.student_assignment_submit.service.AssignmentSubmitService;
import edu.vinu.domain.teacher.service.TeacherStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TeacherStatsServiceImpl implements TeacherStatsService {
    private final BatchService batchService;
    private final ModuleService moduleService;
    private final AssignmentSubmitService assignmentSubmitService;

    @Override
    public DashboardStats getAssignedBatchesCount(Long teacherId) {
        return getAssignedBatchesCount(teacherId, null);
    }

    @Override
    public DashboardStats getAssignedBatchesCount(Long teacherId, BatchStatus batchStatus) {
        return DashboardStats.builder()
                .value(BigDecimal.valueOf(batchService.countAssignedBatchesToTeacher(teacherId, batchStatus)))
                .label("Currently teaching" )
                .build();
    }

    @Override
    public DashboardStats getAssignedModulesCount(Long teacherId) {
        return getAssignedModulesCount(teacherId, null);
    }

    @Override
    public DashboardStats getAssignedModulesCount(Long teacherId, BatchStatus batchStatus) {
        return DashboardStats.builder()
                .value(BigDecimal.valueOf(moduleService.countAssignedModulesToTeacher(teacherId, batchStatus)))
                .label("Across all active batches")
                .build();
    }

    @Override
    public DashboardStats getPendingEvaluationCount(Long teacherId) {
        return DashboardStats.builder()
                .value(BigDecimal.valueOf(assignmentSubmitService.countPendingEvaluationsForTeacher(teacherId)))
                .label("Submissions awaiting review")
                .build();
    }
}
