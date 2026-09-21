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

import edu.vinu.common.exception.custom.BadRequestException;
import edu.vinu.common.exception.custom.InternalServerErrorException;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.assignment.service.AssignmentSecurityService;
import edu.vinu.domain.grading.entity.GradingRangeEntity;
import edu.vinu.domain.grading.enums.GradingEligibilityReason;
import edu.vinu.domain.grading.request.GradingSubmissionRequest;
import edu.vinu.domain.grading.response.GradingEligibilityResponse;
import edu.vinu.domain.grading.service.GradingRangeService;
import edu.vinu.domain.grading.service.GradingService;
import edu.vinu.domain.student_assignment_submit.entity.StudentAssignmentSubmit;
import edu.vinu.domain.student_assignment_submit.enums.AssignmentSubmitStatus;
import edu.vinu.domain.student_assignment_submit.repository.StudentAssignmentSubmitRepository;
import edu.vinu.domain.grading.response.SubmissionGradedResponse;
import edu.vinu.domain.student_assignment_submit.service.AssignmentSubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradingServiceImpl implements GradingService {
    private final AssignmentSubmitService assignmentSubmitService;
    private final StudentAssignmentSubmitRepository assignmentSubmitRepository;
    private final AssignmentSecurityService assignmentSecurityService;
    private final GradingRangeService gradingRangeService;

    @Transactional
    @Override
    public ApiResponse gradeSubmission(Long submissionId, GradingSubmissionRequest request) {
        StudentAssignmentSubmit submission = assignmentSubmitService.getSubmissionEntityById(submissionId);

        if(submission.getGrade() != null || submission.getStatus() == AssignmentSubmitStatus.GRADED){
            throw new BadRequestException("Submission has already been graded.");
        }

        Long assignmentId = submission.getAssignment().getId();

        assignmentSecurityService.validateAssignmentAccess(assignmentId);

        List<GradingRangeEntity> gradingRanges = gradingRangeService.getAllGradingRangersByAssignmentId(assignmentId);

        validateMarksGained(request.marksGained(), submission.getAssignment().getTotalMarks());

        if (gradingRanges.isEmpty()) {
            throw new NotFoundException("Grading Schema not found for the assignment.");
        }

        String receivedGrade = null;
        for (GradingRangeEntity gradingRange : gradingRanges) {
            if (request.marksGained() >= gradingRange.getMinMarks() && request.marksGained() <= gradingRange.getMaxMarks()) {
                submission.setGrade(gradingRange.getDesiredGrade());
                submission.setMarksGained(request.marksGained());
                submission.setStatus(AssignmentSubmitStatus.GRADED);
                assignmentSubmitRepository.save(submission);
                receivedGrade = gradingRange.getDesiredGrade();
                break;
            }
        }

        if(receivedGrade == null){
            throw new NotFoundException("No grading range found for the provided marks gained: " + request.marksGained());
        }

        return ApiResponse.builder()
                .message("Submission graded successfully.")
                .data(SubmissionGradedResponse.builder()
                        .submissionId(submission.getId())
                        .grade(receivedGrade)
                        .marksGained(submission.getMarksGained())
                        .build())
                .build();
    }

    @Override
    public ApiResponse checkEligibility(Long submissionId) {
        StudentAssignmentSubmit submission = assignmentSubmitService.getSubmissionEntityById(submissionId);

        GradingEligibilityResponse response;

        if(submission.getGrade() != null || submission.getStatus() == AssignmentSubmitStatus.GRADED){
            response = new GradingEligibilityResponse(false, GradingEligibilityReason.ALREADY_GRADED);
        }else{
            response = new GradingEligibilityResponse(true, GradingEligibilityReason.ELIGIBLE);
        }

        return ApiResponse.builder().message("Grading eligibility checked!").data(response).build();
    }

    private void validateMarksGained(int marksGained, int totalMarks) {
        if (marksGained < 0 || marksGained > totalMarks) {
            throw new InvalidInputException("marksGained","Marks gained must be between 0 and " + totalMarks);
        }
    }
}
