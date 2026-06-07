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

package edu.vinu.domain.assignment.service.impl;

import edu.vinu.domain.assignment.entity.AssignmentEntity;
import edu.vinu.domain.assignment.entity.ModuleAssignmentEntity;
import edu.vinu.domain.assignment.service.AssignmentService;
import edu.vinu.domain.assignment.service.ChapterAssignmentQueryService;
import edu.vinu.domain.assignment.service.ModuleAssignmentService;
import edu.vinu.domain.module.entity.ModuleEntity;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.domain.assignment.mapper.AssignmentMapper;
import edu.vinu.domain.assignment.mapper.ModuleAssignmentMapper;
import edu.vinu.domain.assignment.repository.ModuleAssignmentRepository;
import edu.vinu.domain.assignment.request.module_assignments.ModuleAssignmentCreateRequest;
import edu.vinu.domain.assignment.response.module_assignment.ModuleAssignmentResponse;
import edu.vinu.domain.module.service.ModuleService;
import edu.vinu.domain.assignment.validator.AssignmentValidator;
import edu.vinu.domain.grading.validator.GradingRangeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ModuleAssignmentServiceImpl implements ModuleAssignmentService {
    private final ModuleAssignmentRepository moduleAssignmentRepository;
    private final AssignmentService assignmentService;
    private final ChapterAssignmentQueryService chapterAssignmentQueryService;
    private final ModuleService moduleService;

    @Transactional
    @Override
    public ModuleAssignmentResponse createModuleAssignment(ModuleAssignmentCreateRequest request, MultipartFile file) {
        AssignmentEntity assignmentEntity = null;

        try {
            AssignmentValidator.validateCreate(request.totalMarks(), request.availableOn(), request.dueDate(), request.resubmission(), request.maxAttempts());
            GradingRangeValidator.validate(request.gradingRanges(), request.totalMarks());

            assignmentEntity = assignmentService.createAssignment(AssignmentMapper.toAssignmentCreateRequest(request),file);

            ModuleEntity moduleEntity = moduleService.getModuleEntityById(request.moduleId());

            if(chapterAssignmentQueryService.existsByAssignmentId(assignmentEntity.getId())){
                throw new InvalidInputException("Assignment is already exists with chapter");
            }

            ModuleAssignmentEntity moduleAssignmentEntity = ModuleAssignmentMapper.toModuleAssignmentEntity(assignmentEntity, moduleEntity);

            ModuleAssignmentEntity savedModuleAssignmentEntity = moduleAssignmentRepository.save(moduleAssignmentEntity);

            return ModuleAssignmentMapper.toModuleAssignmentResponse(savedModuleAssignmentEntity);
        } catch (Exception e) {

            if (assignmentEntity != null) {
                assignmentService.deleteAssignmentFile(
                        assignmentEntity.getFileName()
                );
            }

            throw e;
        }
    }
}
