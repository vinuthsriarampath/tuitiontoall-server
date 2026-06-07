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
import edu.vinu.entity.ChapterEntity;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.mapper.AssignmentMapper;
import edu.vinu.mapper.ChapterAssignmentMapper;
import edu.vinu.repository.ChapterAssignmentRepository;
import edu.vinu.request.assignments.chapter_assignments.ChapterAssignmentCreateRequest;
import edu.vinu.response.assignments.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.service.common.*;
import edu.vinu.validator.AssignmentValidator;
import edu.vinu.validator.GradingRangeValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class ChapterAssignmentServiceImpl implements ChapterAssignmentService {
    private final ChapterAssignmentRepository chapterAssignmentRepository;
    private final AssignmentService assignmentService;
    private final ModuleAssignmentQueryService moduleAssignmentQueryService;
    private final ChapterService chapterService;

    @Override
    @Transactional
    public ChapterAssignmentResponse createChapterAssignment(ChapterAssignmentCreateRequest request, MultipartFile file) {

        AssignmentEntity assignmentEntity = null;

        try {
            AssignmentValidator.validateCreate(request.totalMarks(), request.availableOn(), request.dueDate(), request.resubmission(), request.maxAttempts());
            GradingRangeValidator.validate(request.gradingRanges(), request.totalMarks());

            assignmentEntity = assignmentService.createAssignment(AssignmentMapper.toAssignmentCreateRequest(request),file);

            ChapterEntity chapterEntity = chapterService.getChapterEntityById(request.chapterId());

            if(moduleAssignmentQueryService.existsByAssignmentId(assignmentEntity.getId())){
                throw new InvalidInputException("Assignment is already exists with module");
            }

            ChapterAssignmentEntity chapterAssignmentEntity = ChapterAssignmentMapper.toChapterAssignmentEntity(assignmentEntity,chapterEntity);

            ChapterAssignmentEntity savedChapterAssignmentEntity = chapterAssignmentRepository.save(chapterAssignmentEntity);

            return ChapterAssignmentMapper.toChapterAssignmentResponse(savedChapterAssignmentEntity);
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
