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

import edu.vinu.request.ApplicationRejectionRequest;
import edu.vinu.request.ApplicationSelectionRequest;
import edu.vinu.response.*;
import edu.vinu.service.common.InstituteTeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PaginatedApiResponse<InstituteTeacherResponse>> getAllTeacherByInstitute(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "joinedDate") String sortBy
    ){

        Page<InstituteTeacherResponse> instituteTeacherResponse = instituteTeacherService.getAllTeachersByInstitute(page, size, direction, sortBy);
        return ResponseEntity.status(200).body(
                PaginatedApiResponse.<InstituteTeacherResponse>builder()
                        .message("Teachers related to the institute fetched successfully!")
                        .data(instituteTeacherResponse.getContent())
                        .page(instituteTeacherResponse.getNumber())
                        .size(instituteTeacherResponse.getSize())
                        .totalPages(instituteTeacherResponse.getTotalPages())
                        .totalElements(instituteTeacherResponse.getTotalElements())
                        .last(instituteTeacherResponse.isLast())
                        .build()
        );
    }

}
