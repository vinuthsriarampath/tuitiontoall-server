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

package edu.vinu.domain.student_assignment_submit.service.impl;

import edu.vinu.common.exception.custom.InternalServerErrorException;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.assignment.entity.AssignmentEntity;
import edu.vinu.domain.assignment.service.AssignmentSecurityService;
import edu.vinu.domain.assignment.service.AssignmentService;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.student.service.StudentService;
import edu.vinu.domain.student_assignment_submit.entity.StudentAssignmentSubmit;
import edu.vinu.domain.student_assignment_submit.enums.AssignmentSubmitStatus;
import edu.vinu.domain.student_assignment_submit.enums.SubmissionEligibilityReason;
import edu.vinu.domain.student_assignment_submit.mapper.AssignmentSubmissionMapper;
import edu.vinu.domain.student_assignment_submit.repository.StudentAssignmentSubmitRepository;
import edu.vinu.domain.student_assignment_submit.response.AssignmentSubmissionEligibilityResponse;
import edu.vinu.domain.student_assignment_submit.service.AssignmentSubmitService;
import edu.vinu.infastructure.service.file_storage.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentSubmitServiceImpl implements AssignmentSubmitService {
    private final StudentAssignmentSubmitRepository submitRepository;
    private final FileService fileService;
    private final AssignmentService assignmentService;
    private final StudentService studentService;
    private final AssignmentSecurityService assignmentSecurityService;
    
    @Value("${file.assignment-submission-path}")
    private String submissionPath;

    @Override
    public ApiResponse submit(Long assignmentId, MultipartFile file) {

        StudentEntity currentStudent = studentService.getCurrentStudent();

        if(file.isEmpty()){
            throw new InvalidInputException("file", "File is required for submission.");
        }

        assignmentSecurityService.validateAssignmentAccess(assignmentId);

        AssignmentEntity assignmentEntity = assignmentService.getAssignmentEntity(assignmentId);
        int submissionCount = getSubmissionCount(assignmentId, currentStudent.getId());

        checkEligibility(assignmentEntity, submissionCount);

        String fileName = generateFileName(file.getOriginalFilename(), currentStudent, assignmentEntity, submissionCount);

        try {
            fileService.saveFile(file, getSubmissionPath(), fileName, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to save the submission file. Please try again.");
        }

        AssignmentSubmitStatus status = AssignmentSubmitStatus.SUBMITTED;

        if (assignmentEntity.isLateSubmission() && assignmentEntity.getDueDate().isBefore(LocalDateTime.now()) ) {
            status = AssignmentSubmitStatus.LATE_SUBMITTED;
        }

        if (assignmentEntity.isResubmission() && submissionCount > 0) {
            status = AssignmentSubmitStatus.RESUBMITTED;
        }

        StudentAssignmentSubmit submission = StudentAssignmentSubmit.builder()
                .url(fileName)
                .status(status)
                .attemptNo(submissionCount+1)
                .student(currentStudent)
                .assignment(assignmentEntity)
                .build();

        try {
            StudentAssignmentSubmit save = submitRepository.save(submission);
            return ApiResponse.builder().message("Assignment submitted successfully.").data(AssignmentSubmissionMapper.toAssignmentSubmissionResponse(save)).build();
        } catch (Exception e) {
            fileService.delete(getSubmissionPath(fileName));
            throw new InternalServerErrorException("Failed to save the submission record. Please try again.");
        }
    }

    @Override
    public ApiResponse checkEligibility(Long assignmentId) {

        assignmentSecurityService.validateAssignmentAccess(assignmentId);

        StudentEntity currentStudent = studentService.getCurrentStudent();
        AssignmentEntity assignmentEntity = assignmentService.getAssignmentEntity(assignmentId);
        int submissionCount = getSubmissionCount(assignmentId, currentStudent.getId());

        AssignmentSubmissionEligibilityResponse response = new AssignmentSubmissionEligibilityResponse();

        if (!validateLateSubmission(assignmentEntity)){
            response.setCanSubmit(false);
            response.setReason(SubmissionEligibilityReason.LATE_SUBMISSION_NOT_ALLOWED);
        }else if (!validateMaxAttempts(assignmentEntity, submissionCount)){
            response.setCanSubmit(false);
            response.setReason(SubmissionEligibilityReason.MAX_ATTEMPTS_REACHED);
        }else if (!validateResubmission(assignmentEntity, submissionCount)){
            response.setCanSubmit(false);
            response.setReason(SubmissionEligibilityReason.RESUBMISSION_NOT_ALLOWED);
        }else {
            response.setCanSubmit(true);
            response.setReason(SubmissionEligibilityReason.ELIGIBLE);
        }

        return ApiResponse.builder().message("Assignment submission eligibility checked.").data(response).build();
    }

    private void checkEligibility(AssignmentEntity assignmentEntity, int submissionCount) {
        if (!validateLateSubmission(assignmentEntity)){
            throw new InvalidInputException("Late submissions are not allowed for this assignment.");
        }else if (!validateMaxAttempts(assignmentEntity, submissionCount)){
            throw new InvalidInputException("Maximum attempts reached for this assignment.");
        }else if (!validateResubmission(assignmentEntity, submissionCount)){
            throw new InvalidInputException("Resubmissions are not allowed for this assignment.");
        }
    }


    private int getSubmissionCount(Long assignmentId, Long studentId) {
        return submitRepository.countSubmitsByAssignmentAndStudent(assignmentId, studentId);
    }

    private boolean validateLateSubmission(AssignmentEntity assignmentEntity) {
        if (assignmentEntity.getDueDate().isBefore(LocalDateTime.now())){
            return assignmentEntity.isLateSubmission();
        }
        return  true;
    }

    private boolean validateResubmission(AssignmentEntity assignmentEntity, int submissionCount) {
        if (submissionCount == 0){
            return true;
        }else {
            return assignmentEntity.isResubmission();
        }
    }

    private boolean validateMaxAttempts(AssignmentEntity assignmentEntity, int submissionCount) {
        return submissionCount < assignmentEntity.getMaxAttempts();
    }

    private Path getSubmissionPath(){
        return Path.of(submissionPath);
    }

    private Path getSubmissionPath(String fileName){
        Path path = Path.of(submissionPath, fileName);
        if (!Files.exists(path)) throw new NotFoundException("Submission file not found.");
        return path;
    }

    private String generateFileName(String originalFilename,StudentEntity currentStudent, AssignmentEntity assignmentEntity, int submissionCount) {
        String extension = fileService.extractExtension(originalFilename);
        if (!extension.startsWith(".")) {
            extension = "." + extension;
        }
        String namePart = (currentStudent.getFirstName() + currentStudent.getLastName()).toLowerCase();
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        String assignmentIdPart = assignmentEntity.getId().toString()+(submissionCount+1);

        return String.format("%s_%s_%s_%s%s", namePart, assignmentIdPart , datePart, uuidPart, extension);
    }
}