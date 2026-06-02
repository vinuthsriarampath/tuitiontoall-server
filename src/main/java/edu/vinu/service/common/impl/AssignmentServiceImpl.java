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
import edu.vinu.exception.custom.InternalServerErrorException;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.mapper.AssignmentMapper;
import edu.vinu.repository.AssignmentRepository;
import edu.vinu.request.assignments.AssignmentCreateRequest;
import edu.vinu.service.common.AssignmentService;
import edu.vinu.service.common.FileService;
import edu.vinu.service.common.GradingRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
