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
import edu.vinu.common.exception.custom.InternalServerErrorException;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.assignment.mapper.AssignmentMapper;
import edu.vinu.domain.assignment.service.AssignmentService;
import edu.vinu.domain.grading.mapper.GradingRangeMapper;
import edu.vinu.domain.assignment.repository.AssignmentRepository;
import edu.vinu.domain.assignment.request.AssignmentCreateRequest;
import edu.vinu.domain.assignment.request.AssignmentUpdateRequest;
import edu.vinu.domain.assignment.response.AssignmentDetailedResponse;
import edu.vinu.domain.grading.response.GradingRageResponse;
import edu.vinu.infastructure.service.file_storage.FileService;
import edu.vinu.domain.grading.service.GradingRangeService;
import edu.vinu.domain.assignment.validator.AssignmentValidator;
import edu.vinu.domain.grading.validator.GradingRangeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final GradingRangeService gradingRangeService;
    private final FileService fileService;

    @Value("${file.assignment-path}")
    private String assignmentPath;

    @Transactional
    @Override
    public AssignmentEntity createAssignment(AssignmentCreateRequest request, MultipartFile file) {

        final String savedAssignmentFileName = saveAssignmentFile(file);

        try {

            AssignmentEntity entity = AssignmentMapper.toAssignmentEntity(request,savedAssignmentFileName);

            AssignmentEntity savedAssignmentEntity = assignmentRepository.save(entity);

            gradingRangeService.saveGradingRangeList(request.gradingRanges(), savedAssignmentEntity);

            return savedAssignmentEntity;

        } catch (InvalidInputException e) {
            deleteAssignmentFile(savedAssignmentFileName);
            throw e;
        } catch (Exception e) {
            deleteAssignmentFile(savedAssignmentFileName);
            throw new InternalServerErrorException("Failed to create assignment");
        }
    }

    @Override
    public void deleteAssignmentFile(String fileName) {
        fileService.delete(Path.of(assignmentPath, fileName));
    }

    @Transactional
    @Override
    public AssignmentDetailedResponse updateAssignment(Long id, AssignmentUpdateRequest request) {
        AssignmentEntity existing = getAssignmentEntity(id);

        AssignmentValidator.validateUpdate(existing, request);
        GradingRangeValidator.validate(request.gradingRanges(),request.totalMarks());

        existing.setTopic(request.topic());
        existing.setDescription(request.description());
        existing.setTotalMarks(request.totalMarks());
        existing.setAvailableOn(request.availableOn());
        existing.setDueDate(request.dueDate());
        existing.setLateSubmission(request.lateSubmission());
        existing.setResubmission(request.resubmission());
        existing.setMaxAttempts(request.maxAttempts());

        AssignmentEntity savedAssignmentEntity = assignmentRepository.save(existing);

        List<GradingRageResponse> gradingRageResponses = gradingRangeService.updateGradingRange(savedAssignmentEntity, request.gradingRanges());

        return AssignmentMapper.toAssignmentDetailedResponse(savedAssignmentEntity, gradingRageResponses);
    }

    @Override
    public String updateAssignmentFile(Long id, MultipartFile file) {
        AssignmentEntity existing = getAssignmentEntity(id);

        String oldFileName = existing.getFileName();

        String newFileName = saveAssignmentFile(file);

        existing.setFileName(newFileName);

        try {
            assignmentRepository.save(existing);
            deleteAssignmentFile(oldFileName);
            return newFileName;
        } catch (Exception e) {
            deleteAssignmentFile(newFileName);
            throw new InternalServerErrorException("Failed to update assignment file!");
        }
    }

    @Override
    public AssignmentDetailedResponse getDetailedAssignmentById(Long id) {
        AssignmentEntity assignmentEntity = getAssignmentEntity(id);
        List<GradingRageResponse> gradingRangers = gradingRangeService.getAllGradingRangersByAssignmentId(id).stream()
                .map(GradingRangeMapper::toGradingRageResponse)
                .toList();
        return AssignmentMapper.toAssignmentDetailedResponse(assignmentEntity, gradingRangers);
    }

    private AssignmentEntity getAssignmentEntity(Long id){
        return assignmentRepository.findById(id).orElseThrow(()-> new NotFoundException("Assignment not found by id!"));
    }

    private String saveAssignmentFile(MultipartFile file) {
        final String originalFilename = file.getOriginalFilename();
        final String fileName = generateFileName(originalFilename);
        fileService.saveFile(file, getAssignmentPath(), fileName, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }


    private String generateFileName(String originalFilename) {
        final String extension = fileService.extractExtension(originalFilename);
        return UUID.randomUUID()+extension;
    }

    private Path getAssignmentPath(){
        return Path.of(assignmentPath);
    }

}
