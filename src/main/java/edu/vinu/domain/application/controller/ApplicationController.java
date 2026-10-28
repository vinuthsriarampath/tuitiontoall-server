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

package edu.vinu.domain.application.controller;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.application.dto.Application;
import edu.vinu.domain.application.request.ApplicationFilterRequest;
import edu.vinu.domain.application.response.ApplicationDetailsResponse;
import edu.vinu.domain.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequestMapping("/api/v2/applications")
@RequiredArgsConstructor
public class ApplicationController {

    public final ApplicationService applicationService;

    @PreAuthorize("hasAuthority('teacher')")
    @PostMapping("/vacancies/{vacancyId}/apply")
    public ResponseEntity<ApiResponse<Application>> createApplication(@PathVariable Long vacancyId){
        Application application = applicationService.createApplication(vacancyId);
        return ResponseEntity.status(201).body(createApiResponse("Application created successfully", application));
    }

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkIfUserAlreadyApplied(@RequestParam("teacherId") Long userId,@RequestParam("vacancyId") Long vacancyId){
        boolean userAlreadyApplied = applicationService.isUserAlreadyApplied(userId, vacancyId);
        return ResponseEntity.status(200).body(createApiResponse("Success", userAlreadyApplied));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("vacancies/{vacancyId}")
    public ResponseEntity<PaginatedApiResponse<ApplicationDetailsResponse>> getAllApplicationsByVacancy(@PathVariable("vacancyId") Long vacancyId, PaginationRequest pagination, ApplicationFilterRequest filters){
        return ResponseEntity.ok(applicationService.getApplicationsByVacancy(vacancyId,pagination, filters));
    }
}
