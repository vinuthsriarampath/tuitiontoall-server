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

package edu.vinu.service.common.impl;

import edu.vinu.entity.AssignmentEntity;
import edu.vinu.entity.GradingRangeEntity;
import edu.vinu.mapper.GradingRangeMapper;
import edu.vinu.repository.GradingRangeRepository;
import edu.vinu.request.grading_range.GradingRangeCreateRequest;
import edu.vinu.service.common.GradingRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GradingRangeServiceImpl implements GradingRangeService {
    private final GradingRangeRepository gradingRangeRepository;

    @Override
    @Transactional
    public void saveGradingRangeList(List<GradingRangeCreateRequest> gradingRangeCreateRequests, AssignmentEntity savedAssignmentEntity) {

        List<GradingRangeEntity> gradingRangeEntities = gradingRangeCreateRequests
                .stream()
                .map(request -> GradingRangeMapper.toGradingRangeEntity(request, savedAssignmentEntity))
                .toList();

        gradingRangeRepository.saveAll(gradingRangeEntities);

    }
}
