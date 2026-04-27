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

import edu.vinu.request.ApplicationSelectionRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.ApplicationSelectionResponse;
import edu.vinu.service.common.InstituteTeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/institutes/teachers")
@RequiredArgsConstructor
public class InstituteTeacherController {

    private final InstituteTeacherService instituteTeacherService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse> onboardTeachers(@Valid @RequestBody ApplicationSelectionRequest request){
        ApplicationSelectionResponse applicationSelectionResponse = instituteTeacherService.onBoardTeachers(request);
        return ResponseEntity.status(201).body(new ApiResponse("Teachers Onboarded Successfully!", applicationSelectionResponse));
    }
}
