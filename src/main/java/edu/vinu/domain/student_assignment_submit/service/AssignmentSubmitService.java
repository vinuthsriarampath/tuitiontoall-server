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

package edu.vinu.domain.student_assignment_submit.service;


import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.student_assignment_submit.request.AssignmentSubmissionFilterRequest;
import edu.vinu.domain.student_assignment_submit.response.AssignmentSubmissionDetailedResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentSubmitService {
    ApiResponse submit(Long assignmentId, MultipartFile file);

    ApiResponse checkEligibility(Long assignmentId);

    PaginatedApiResponse<AssignmentSubmissionDetailedResponse> getAllSubmissionByAssignment(Long assignmentId, PaginationRequest pagination, AssignmentSubmissionFilterRequest filters);
}
