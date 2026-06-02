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
import edu.vinu.entity.GradingRangeEntity;
import edu.vinu.request.grading_range.GradingRangeCreateRequest;
import edu.vinu.response.grading_range.GradingRageResponse;
import org.springframework.stereotype.Component;

@Component
public class GradingRangeMapper {

    public static GradingRangeEntity toGradingRangeEntity(GradingRangeCreateRequest request, AssignmentEntity assignmentEntity) {
        return GradingRangeEntity.builder()
                .minMarks(request.minMarks())
                .maxMarks(request.maxMarks())
                .desiredGrade(request.desiredGrade())
                .description(request.description())
                .assignment(assignmentEntity)
                .build();
    }

    public static GradingRageResponse toGradingRageResponse(GradingRangeEntity entity){
        return GradingRageResponse.builder()
                .id(entity.getId())
                .assignmentId(entity.getAssignment().getId())
                .minMarks(entity.getMinMarks())
                .maxMarks(entity.getMaxMarks())
                .desiredGrade(entity.getDesiredGrade())
                .description(entity.getDescription())
                .build();
    }
}
