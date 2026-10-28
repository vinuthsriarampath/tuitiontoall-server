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

package edu.vinu.domain.institute.controller;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.application.request.ApplicationRejectionRequest;
import edu.vinu.domain.application.request.ApplicationSelectionRequest;
import edu.vinu.domain.application.response.ApplicationRejectionResponse;
import edu.vinu.domain.application.response.ApplicationSelectionResponse;
import edu.vinu.domain.institute.request.InstituteTeacherFilterRequest;
import edu.vinu.domain.institute.response.InstituteTeacherResponse;
import edu.vinu.domain.institute.response.InstituteTeacherStatsResponse;
import edu.vinu.domain.institute.service.InstituteTeacherService;
import edu.vinu.domain.teacher.dtos.response.TeacherBasicResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequestMapping("/api/v2/institutes/teachers")
@RequiredArgsConstructor
public class InstituteTeacherController {

    private final InstituteTeacherService instituteTeacherService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/onboard")
    public ResponseEntity<ApiResponse<ApplicationSelectionResponse>> onboardTeachers(@Valid @RequestBody ApplicationSelectionRequest request){
        return ResponseEntity.status(201).body(createApiResponse("Teachers Onboarded Successfully!", instituteTeacherService.onBoardTeachers(request)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/reject")
    public ResponseEntity<ApiResponse<ApplicationRejectionResponse>> rejectApplications(@Valid @RequestBody ApplicationRejectionRequest request){
        return ResponseEntity.ok(createApiResponse("Applications Rejected Successfully!", instituteTeacherService.rejectApplications(request)));
    }

    @PreAuthorize(("hasAuthority('institute')"))
    @GetMapping
    public ResponseEntity<PaginatedApiResponse<InstituteTeacherResponse>> getAllTeacherByInstitute(PaginationRequest pagination, InstituteTeacherFilterRequest filters){
        return ResponseEntity.ok(instituteTeacherService.getAllTeachersByInstitute(pagination, filters));
    }

    @PreAuthorize(("hasAuthority('institute')"))
    @GetMapping("/basic")
    public ResponseEntity<ApiResponse<List<TeacherBasicResponse>>> getAllTeacherByCurrentInstitute(){
        return ResponseEntity.ok(createApiResponse("Teachers related to the institute fetched successfully!", instituteTeacherService.getAllTeachersByCurrentInstitute()));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<InstituteTeacherStatsResponse>> getInstituteTeacherStats(){
        return ResponseEntity.ok(createApiResponse("Institute teacher stats fetched successfully!",instituteTeacherService.getInstituteTeacherStats()));
    }

}
