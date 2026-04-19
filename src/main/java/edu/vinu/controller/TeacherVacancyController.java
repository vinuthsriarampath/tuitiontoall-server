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

import edu.vinu.model.TeacherVacancy;
import edu.vinu.request.CreateVacancyRequest;
import edu.vinu.request.UpdateVacancyRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.PaginatedApiResponse;
import edu.vinu.service.common.TeacherVacancyService;
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
    public ResponseEntity<PaginatedApiResponse<TeacherVacancy>> getByInstitute(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "createdDate") String sortBy, @RequestParam(defaultValue = "desc") String direction) {
        Page<TeacherVacancy> pageData  = vacancyService.getAllByInstitute(page, size, sortBy, direction);

        PaginatedApiResponse<TeacherVacancy> response = PaginatedApiResponse.<TeacherVacancy>builder()
                .message("Vacancies By Institute!")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/{vacancyId}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long vacancyId) {
        return ResponseEntity.status(200).body(new ApiResponse("Found Vacancy By Id",vacancyService.getById(vacancyId)));
    }
}
