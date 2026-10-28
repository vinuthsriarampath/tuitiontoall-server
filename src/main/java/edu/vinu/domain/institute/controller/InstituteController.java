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


import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.domain.institute.request.InstituteDetailsUpdateRequest;
import edu.vinu.domain.institute.response.InstituteBootstrapResponse;
import edu.vinu.domain.institute.service.InstituteBootstrapService;
import edu.vinu.domain.institute.service.InstituteService;
import edu.vinu.domain.teacher_vacancy.dto.TeacherVacancy;
import edu.vinu.domain.teacher_vacancy.service.TeacherVacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v2/institutes")
@RequiredArgsConstructor
public class InstituteController {

    private final InstituteBootstrapService instituteBootstrapService;
    private final InstituteService instituteService;
    private final TeacherVacancyService vacancyService;

    @GetMapping("/{instituteId}/vacancies")
    public ResponseEntity<ApiResponse<List<TeacherVacancy>>> getVacanciesByStatusAndInstituteId(@PathVariable(name = "instituteId") Long instituteId, @RequestParam(defaultValue = "OPEN") String status) {
        return ResponseEntity.status(200).body(createApiResponse("All Vacancies By provided status", vacancyService.getAllByInstituteIdAndStatus(instituteId, status)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Institute>> updateInstituteDetails(@Valid @RequestBody InstituteDetailsUpdateRequest instituteDetailsUpdateRequest) {
        Institute updateInstituteDetails = instituteService.updateInstituteDetails(SecurityContextHolder.getContext().getAuthentication().getName(), instituteDetailsUpdateRequest);
        return ResponseEntity.status(OK).body(createApiResponse("Profile Updated", updateInstituteDetails));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("validate/role")
    public ResponseEntity<ApiResponse<Void>> validateInstituteRole() {
        return ResponseEntity.status(OK).body(createApiResponse("User has institute role!", null));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/me/bootstrap")
    public ResponseEntity<ApiResponse<InstituteBootstrapResponse>> getInstituteBootstrapData() {
        return ResponseEntity.ok(instituteBootstrapService.getCurrentInstituteBootstrapData());
    }
}
