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
import edu.vinu.entity.ChapterAssignmentEntity;
import edu.vinu.entity.ChapterEntity;
import edu.vinu.response.assignments.chapter_assignment.ChapterAssignmentResponse;
import org.springframework.stereotype.Component;

@Component
public class ChapterAssignmentMapper {

    public static ChapterAssignmentEntity toChapterAssignmentEntity(AssignmentEntity assignmentEntity, ChapterEntity chapterEntity) {
        return ChapterAssignmentEntity.builder()
                .assignment(assignmentEntity)
                .chapter(chapterEntity)
                .build();
    }

    public static ChapterAssignmentResponse toChapterAssignmentResponse(ChapterAssignmentEntity entity) {
        return ChapterAssignmentResponse.builder()
                .id(entity.getId())
                .chapterId(entity.getChapter().getId())
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
}
