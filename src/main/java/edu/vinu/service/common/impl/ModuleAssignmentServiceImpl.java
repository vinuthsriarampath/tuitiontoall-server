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
import edu.vinu.entity.ChapterAssignmentEntity;
import edu.vinu.entity.ModuleAssignmentEntity;
import edu.vinu.entity.ModuleEntity;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.mapper.AssignmentMapper;
import edu.vinu.mapper.ChapterAssignmentMapper;
import edu.vinu.mapper.ModuleAssignmentMapper;
import edu.vinu.repository.ModuleAssignmentRepository;
import edu.vinu.request.assignments.module_assignments.ModuleAssignmentCreateRequest;
import edu.vinu.response.assignments.module_assignment.ModuleAssignmentResponse;
import edu.vinu.service.common.AssignmentService;
import edu.vinu.service.common.ChapterAssignmentQueryService;
import edu.vinu.service.common.ModuleAssignmentService;
import edu.vinu.service.common.ModuleService;
import edu.vinu.validator.AssignmentValidator;
import edu.vinu.validator.GradingRangeValidator;
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
