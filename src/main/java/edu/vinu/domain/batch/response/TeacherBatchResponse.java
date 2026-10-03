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

package edu.vinu.domain.batch.response;

import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.module.response.TeacherModuleResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record TeacherBatchResponse (
        Long batchId,
        String batchName,
        LocalDate batchStartDate,
        LocalTime batchStartTime,
        BatchStatus batchStatus,
        List<TeacherModuleResponse> modules
){
    public TeacherBatchResponse{
        modules = modules == null ? List.of() : List.copyOf(modules);
    }
}
