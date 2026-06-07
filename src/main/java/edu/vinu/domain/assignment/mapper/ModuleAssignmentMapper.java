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

package edu.vinu.domain.assignment.mapper;

import edu.vinu.domain.assignment.entity.AssignmentEntity;
import edu.vinu.domain.assignment.entity.ModuleAssignmentEntity;
import edu.vinu.domain.assignment.repository.projection.ModuleAssignmentProjection;
import edu.vinu.domain.assignment.response.module_assignment.ModuleAssignmentResponse;
import edu.vinu.domain.module.entity.ModuleEntity;

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
