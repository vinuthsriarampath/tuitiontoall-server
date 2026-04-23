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

package edu.vinu.controller;

import edu.vinu.model.Application;
import edu.vinu.response.ApiResponse;
import edu.vinu.service.common.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/applications")
@RequiredArgsConstructor
public class ApplicationController {

    public final ApplicationService applicationService;

    @PreAuthorize("hasAuthority('teacher')")
    @PostMapping("/vacancies/{vacancyId}/apply")
    public ResponseEntity<ApiResponse> createApplication(@PathVariable Long vacancyId){
        Application application = applicationService.createApplication(vacancyId);
        return ResponseEntity.status(201).body(new ApiResponse("Application created successfully", application));
    }

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/check")
    public ResponseEntity<ApiResponse> checkIfUserAlreadyApplied(@RequestParam("teacherId") Long userId,@RequestParam("vacancyId") Long vacancyId){
        boolean userAlreadyApplied = applicationService.isUserAlreadyApplied(userId, vacancyId);
        return ResponseEntity.status(200).body(new ApiResponse("Success", userAlreadyApplied));
    }
}
