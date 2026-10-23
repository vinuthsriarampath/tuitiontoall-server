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

package edu.vinu.domain.teacher_vacancy.controller;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.teacher_vacancy.dto.TeacherVacancy;
import edu.vinu.domain.teacher_vacancy.enums.TeacherVacancyStatus;
import edu.vinu.domain.teacher_vacancy.request.CreateVacancyRequest;
import edu.vinu.domain.teacher_vacancy.request.TeacherVacancyFilterRequest;
import edu.vinu.domain.teacher_vacancy.request.UpdateVacancyRequest;
import edu.vinu.domain.teacher_vacancy.service.TeacherVacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/vacancies")
@RequiredArgsConstructor
public class TeacherVacancyController {
    private final TeacherVacancyService vacancyService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateVacancyRequest request) {
        return ResponseEntity.status(201).body(new ApiResponse("New Vacancy created Successfully!",vacancyService.createVacancy(request)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{vacancyId}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable Long vacancyId,
            @RequestBody UpdateVacancyRequest request
    ) {
        return ResponseEntity.status(200).body(new ApiResponse("Vacancy Updated Successfully!",vacancyService.updateVacancy(vacancyId, request)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @DeleteMapping("/{vacancyId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long vacancyId) {
        vacancyService.deleteVacancy(vacancyId);
        return ResponseEntity.status(200).body(new ApiResponse("Vacancy Deleted Successfully!", null));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/my")
    public ResponseEntity<PaginatedApiResponse<TeacherVacancy>> getByInstitute(PaginationRequest pagination, TeacherVacancyFilterRequest filters) {
        return ResponseEntity.ok(vacancyService.getAllByInstitute(pagination, filters));
    }

    @GetMapping("/{vacancyId}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long vacancyId,@RequestParam(value = "status",required = false) TeacherVacancyStatus status) {
        if(status != null){
            return ResponseEntity.status(200).body(new ApiResponse("Found Vacancy By Id and Status",vacancyService.getByVacancyIdAndStatus(vacancyId,status)));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Found Vacancy By Id",vacancyService.getById(vacancyId)));
    }
}
