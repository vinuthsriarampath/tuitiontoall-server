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

package edu.vinu.domain.student_assignment_submit.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.student_assignment_submit.service.AssignmentSubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/assignment-submissions")
@RequiredArgsConstructor
public class AssignmentSubmitController {

    private final AssignmentSubmitService submitService;


    @PreAuthorize("hasAuthority('student')")
    @PostMapping("/assignment/{assignmentId}")
    public ResponseEntity<ApiResponse> submit(@PathVariable Long assignmentId, @RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok(submitService.submit(assignmentId, file));
    }

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/assignment/{assignmentId}/eligibility")
    public ResponseEntity<ApiResponse> checkEligibility(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(submitService.checkEligibility(assignmentId));
    }
}
