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

package edu.vinu.domain.student_assignment_submit.mapper;

import edu.vinu.domain.student_assignment_submit.entity.StudentAssignmentSubmit;
import edu.vinu.domain.student_assignment_submit.repository.projections.AssignmentSubmissionDetailedProjection;
import edu.vinu.domain.student_assignment_submit.response.AssignmentSubmissionDetailedResponse;
import edu.vinu.domain.student_assignment_submit.response.AssignmentSubmissionResponse;
import edu.vinu.domain.student_assignment_submit.response.StudentAssignmentSubmissionResponse;

public class AssignmentSubmissionMapper {

    public static AssignmentSubmissionResponse toAssignmentSubmissionResponse(StudentAssignmentSubmit e){
        return AssignmentSubmissionResponse.builder()
                .submissionId(e.getId())
                .studentId(e.getStudent().getId())
                .assignmentId(e.getAssignment().getId())
                .url(e.getUrl())
                .grade(e.getGrade())
                .status(e.getStatus())
                .marksGained(e.getMarksGained())
                .attemptNo(e.getAttemptNo())
                .submittedAt(e.getSubmittedAt())
                .lastModifiedDate(e.getLastModifiedDate())
                .build();
    }

    public static AssignmentSubmissionDetailedResponse toAssignmentSubmissionDetailedResponse(AssignmentSubmissionDetailedProjection p) {
        return AssignmentSubmissionDetailedResponse.builder()
                .submissionId(p.getSubmissionId())
                .studentId(p.getStudentId())
                .studentName(p.getFirstName()+ " " + p.getLastName())
                .assignmentId(p.getAssignmentId())
                .url(p.getUrl())
                .grade(p.getGrade())
                .status(p.getStatus())
                .marksGained(p.getMarksGained())
                .attemptNo(p.getAttemptNo())
                .submittedAt(p.getSubmittedAt())
                .lastModifiedDate(p.getLastModifiedDate())
                .build();
    }

    public static StudentAssignmentSubmissionResponse toStudentAssignmentSubmissionResponse(AssignmentSubmissionDetailedProjection p) {
        return StudentAssignmentSubmissionResponse.builder()
                .submissionId(p.getSubmissionId())
                .assignmentId(p.getAssignmentId())
                .url(p.getUrl())
                .grade(p.getGrade())
                .status(p.getStatus())
                .marksGained(p.getMarksGained())
                .attemptNo(p.getAttemptNo())
                .submittedAt(p.getSubmittedAt())
                .lastModifiedDate(p.getLastModifiedDate())
                .build();
    }
}
