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

package edu.vinu.domain.student_batch_enrollment.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentEligibilityCheckRequest;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentRequest;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasAuthority('student')")
    @PostMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> enrollStudents(@Valid @RequestBody EnrollmentRequest request){
        byte[] invoice = enrollmentService.enrollStudent(request);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"invoice.pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(invoice);
    }

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/eligibility-check")
    public ResponseEntity<ApiResponse> checkEnrolmentEligibility(@Valid EnrollmentEligibilityCheckRequest request){
        ApiResponse response = enrollmentService.checkEnrollmentEligibility(request);
        return ResponseEntity.ok(response);
    }
}
