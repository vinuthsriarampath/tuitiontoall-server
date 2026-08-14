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

package edu.vinu.domain.student_batch_enrollment.service;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.student.dto.response.StudentUserResponse;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentEligibilityCheckRequest;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentRequest;
import jakarta.validation.Valid;

public interface EnrollmentService {
    byte[] enrollStudent(EnrollmentRequest request);

    ApiResponse checkEnrollmentEligibility(EnrollmentEligibilityCheckRequest request);

    PaginatedApiResponse<StudentUserResponse> getStudentsByBatch(Long batchId, PaginationRequest pagination);
}
