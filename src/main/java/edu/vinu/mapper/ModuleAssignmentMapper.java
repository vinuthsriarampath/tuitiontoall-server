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

package edu.vinu.mapper;

import edu.vinu.entity.*;
import edu.vinu.repository.projection.ChapterAssignmentProjection;
import edu.vinu.repository.projection.ModuleAssignmentProjection;
import edu.vinu.response.assignments.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.response.assignments.module_assignment.ModuleAssignmentResponse;

public class ModuleAssignmentMapper {
    public static ModuleAssignmentEntity toModuleAssignmentEntity(AssignmentEntity assignmentEntity, ModuleEntity moduleEntity) {
        return ModuleAssignmentEntity.builder()
                .assignment(assignmentEntity)
                .module(moduleEntity)
                .build();
    }

    public static ModuleAssignmentResponse toModuleAssignmentResponse(ModuleAssignmentEntity entity) {
        return ModuleAssignmentResponse.builder()
                .id(entity.getId())
                .assignmentId(entity.getAssignment().getId())
                .moduleId(entity.getModule().getId())
                .topic(entity.getAssignment().getTopic())
                .description(entity.getAssignment().getDescription())
                .fileName(entity.getAssignment().getFileName())
                .totalMarks(entity.getAssignment().getTotalMarks())
                .availableOn(entity.getAssignment().getAvailableOn())
                .dueDate(entity.getAssignment().getDueDate())
                .lateSubmission(entity.getAssignment().isLateSubmission())
                .reSubmission(entity.getAssignment().isResubmission())
                .maxAttempts(entity.getAssignment().getMaxAttempts())
                .createdDate(entity.getAssignment().getCreatedDate())
                .lastModifiedDate(entity.getAssignment().getLastModifiedDate())
                .build();
    }

    public static ModuleAssignmentResponse toModuleAssignmentResponse(ModuleAssignmentProjection projection) {
        return ModuleAssignmentResponse.builder()
                .id(projection.getId())
                .assignmentId(projection.getAssignmentId())
                .moduleId(projection.getModuleId())
                .topic(projection.getTopic())
                .description(projection.getDescription())
                .fileName(projection.getFileName())
                .totalMarks(projection.getTotalMarks())
                .availableOn(projection.getAvailableOn())
                .dueDate(projection.getDueDate())
                .lateSubmission(projection.getLateSubmission())
                .reSubmission(projection.getResubmission())
                .maxAttempts(projection.getMaxAttempts())
                .createdDate(projection.getCreatedDate())
                .lastModifiedDate(projection.getLastModifiedDate())
                .build();
    }

}
