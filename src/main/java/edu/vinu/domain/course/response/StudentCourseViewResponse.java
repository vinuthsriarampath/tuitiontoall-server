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

package edu.vinu.domain.course.response;

import edu.vinu.domain.batch.response.StudentBatchResponse;
import edu.vinu.domain.module.response.StudentModuleResponse;

import java.util.List;

public record StudentCourseViewResponse (
        StudentCourseResponse course,
        StudentBatchResponse selectedBatch,
        List<StudentModuleResponse> modules
){
    public StudentCourseViewResponse {
        modules = modules == null ? List.of() : List.copyOf(modules);
    }
}
