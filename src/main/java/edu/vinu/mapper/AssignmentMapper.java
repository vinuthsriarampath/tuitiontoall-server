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

import edu.vinu.entity.AssignmentEntity;
import edu.vinu.request.assignments.AssignmentCreateRequest;
import edu.vinu.request.assignments.chapter_assignments.ChapterAssignmentCreateRequest;
import edu.vinu.request.assignments.module_assignments.ModuleAssignmentCreateRequest;
import edu.vinu.response.assignments.AssignmentDetailedResponse;
import edu.vinu.domain.grading.response.GradingRageResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignmentMapper {

    public static AssignmentEntity toAssignmentEntity(AssignmentCreateRequest request, String savedFileName) {
        return AssignmentEntity.builder()
                .topic(request.topic())
                .description(request.description())
                .fileName(savedFileName)
                .totalMarks(request.totalMarks())
                .availableOn(request.availableOn())
                .dueDate(request.dueDate())
                .lateSubmission(request.lateSubmission())
                .resubmission(request.resubmission())
                .maxAttempts(request.maxAttempts())
                .build();
    }

    public static AssignmentCreateRequest toAssignmentCreateRequest(ChapterAssignmentCreateRequest request) {
        return AssignmentCreateRequest.builder()
                .topic(request.topic())
                .description(request.description())
                .fileName(null)
                .totalMarks(request.totalMarks())
                .availableOn(request.availableOn())
                .dueDate(request.dueDate())
                .lateSubmission(request.lateSubmission())
                .resubmission(request.resubmission())
                .maxAttempts(request.maxAttempts())
                .gradingRanges(request.gradingRanges())
                .build();
    }

    public static AssignmentDetailedResponse toAssignmentDetailedResponse(AssignmentEntity assignmentEntity, List<GradingRageResponse> gradingRangers) {
        return AssignmentDetailedResponse.builder()
                .id(assignmentEntity.getId())
                .topic(assignmentEntity.getTopic())
                .description(assignmentEntity.getDescription())
                .fileName(assignmentEntity.getFileName())
                .totalMarks(assignmentEntity.getTotalMarks())
                .availableOn(assignmentEntity.getAvailableOn())
                .dueDate(assignmentEntity.getDueDate())
                .lateSubmission(assignmentEntity.isLateSubmission())
                .reSubmission(assignmentEntity.isResubmission())
                .maxAttempts(assignmentEntity.getMaxAttempts())
                .createdDate(assignmentEntity.getCreatedDate())
                .lastModifiedDate(assignmentEntity.getLastModifiedDate())
                .gradingRangers(gradingRangers)
                .build();
    }

    public static AssignmentCreateRequest toAssignmentCreateRequest(ModuleAssignmentCreateRequest request){
        return AssignmentCreateRequest.builder()
                .topic(request.topic())
                .description(request.description())
                .fileName(null)
                .totalMarks(request.totalMarks())
                .availableOn(request.availableOn())
                .dueDate(request.dueDate())
                .lateSubmission(request.lateSubmission())
                .resubmission(request.resubmission())
                .maxAttempts(request.maxAttempts())
                .gradingRanges(request.gradingRanges())
                .build();
    }
}
