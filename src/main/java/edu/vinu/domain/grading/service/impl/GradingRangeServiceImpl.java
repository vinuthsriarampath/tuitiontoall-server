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

package edu.vinu.domain.grading.service.impl;

import edu.vinu.domain.grading.service.GradingRangeService;
import edu.vinu.entity.AssignmentEntity;
import edu.vinu.domain.grading.entity.GradingRangeEntity;
import edu.vinu.common.exception.custom.InternalServerErrorException;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.grading.mapper.GradingRangeMapper;
import edu.vinu.domain.grading.repository.GradingRangeRepository;
import edu.vinu.domain.grading.request.GradingRangeCreateRequest;
import edu.vinu.domain.grading.request.GradingRangeUpdateRequest;
import edu.vinu.domain.grading.response.GradingRageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public List<GradingRageResponse> updateGradingRange(AssignmentEntity assignmentEntity, List<GradingRangeUpdateRequest> gradingRangeUpdateRequests) {
        List<GradingRangeEntity> existingGradingRangers = getAllGradingRangersByAssignmentId(assignmentEntity.getId());

        Set<Long> existingIds = existingGradingRangers.stream()
                .map(GradingRangeEntity::getId)
                .collect(Collectors.toSet());

        for (GradingRangeUpdateRequest request : gradingRangeUpdateRequests) {

            if (request.id() != null && !existingIds.contains(request.id())) {
                throw new NotFoundException("GradingRange with id " + request.id() + " not found");
            }
        }

        try {
            Set<Long> requestIds = gradingRangeUpdateRequests.stream()
                    .map(GradingRangeUpdateRequest::id)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            List<GradingRangeEntity> rangesToDelete =
                    existingGradingRangers.stream()
                            .filter(range -> !requestIds.contains(range.getId()))
                            .toList();

            gradingRangeRepository.deleteAll(rangesToDelete);
            gradingRangeRepository.flush();

            Map<Long, GradingRangeEntity> existingMap =
                    existingGradingRangers.stream()
                            .collect(Collectors.toMap(
                                    GradingRangeEntity::getId,
                                    Function.identity()
                            ));

            List<GradingRangeEntity> savedRanges = new ArrayList<>();

            for (GradingRangeUpdateRequest request : gradingRangeUpdateRequests) {

                GradingRangeEntity entity;

                if (request.id() != null) {
                    entity = existingMap.get(request.id());
                } else {
                    entity = new GradingRangeEntity();
                    entity.setAssignment(assignmentEntity);
                }

                entity.setMinMarks(request.minMarks());
                entity.setMaxMarks(request.maxMarks());
                entity.setDesiredGrade(request.desiredGrade());
                entity.setDescription(request.description());

                savedRanges.add(entity);
            }

            gradingRangeRepository.saveAll(savedRanges);

            return savedRanges.stream()
                    .map(GradingRangeMapper::toGradingRageResponse)
                    .toList();

        } catch (DataIntegrityViolationException e){
            throw new InvalidInputException("Grading range with the same minMarks and maxMarks already exists for this assignment!");
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public List<GradingRangeEntity> getAllGradingRangersByAssignmentId(Long assignmentId) {
        return gradingRangeRepository.findAllByAssignmentId(assignmentId);
    }
}
