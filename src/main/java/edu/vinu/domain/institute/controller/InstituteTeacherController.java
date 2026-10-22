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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/reject")
    public ResponseEntity<ApiResponse> rejectApplications(@Valid @RequestBody ApplicationRejectionRequest request){
        ApplicationRejectionResponse applicationRejectionResponse = instituteTeacherService.rejectApplications(request);
        return ResponseEntity.status(200).body(new ApiResponse("Applications Rejected Successfully!", applicationRejectionResponse));
    }

    @PreAuthorize(("hasAuthority('institute')"))
    @GetMapping
    public ResponseEntity<PaginatedApiResponse<InstituteTeacherResponse>> getAllTeacherByInstitute(PaginationRequest pagination, InstituteTeacherFilterRequest filters){
        return ResponseEntity.ok(instituteTeacherService.getAllTeachersByInstitute(pagination, filters));
    }

    @PreAuthorize(("hasAuthority('institute')"))
    @GetMapping("/basic")
    public ResponseEntity<ApiResponse> getAllTeacherByCurrentInstitute(){
        List<TeacherBasicResponse> allTeachersByCurrentInstitute = instituteTeacherService.getAllTeachersByCurrentInstitute();
        return ResponseEntity.ok(new ApiResponse("Teachers related to the institute fetched successfully!", allTeachersByCurrentInstitute));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse> getInstituteTeacherStats(){
        InstituteTeacherStatsResponse response = instituteTeacherService.getInstituteTeacherStats();
        return ResponseEntity.status(200).body(new ApiResponse("Institute teacher stats fetched successfully!",response));
    }

}
