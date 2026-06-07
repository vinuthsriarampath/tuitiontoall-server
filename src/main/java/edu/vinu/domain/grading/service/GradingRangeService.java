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

package edu.vinu.domain.grading.service;

import edu.vinu.domain.assignment.entity.AssignmentEntity;
import edu.vinu.domain.grading.entity.GradingRangeEntity;
import edu.vinu.domain.grading.request.GradingRangeCreateRequest;
import edu.vinu.domain.grading.request.GradingRangeUpdateRequest;
import edu.vinu.domain.grading.response.GradingRageResponse;

import java.util.List;

public interface GradingRangeService {
    void saveGradingRangeList(List<GradingRangeCreateRequest> gradingRangeCreateRequests, AssignmentEntity savedAssignmentEntity);

    List<GradingRageResponse> updateGradingRange(AssignmentEntity assignmentEntity, List<GradingRangeUpdateRequest> gradingRangeUpdateRequests);

    List<GradingRangeEntity> getAllGradingRangersByAssignmentId(Long assignmentId);
}
