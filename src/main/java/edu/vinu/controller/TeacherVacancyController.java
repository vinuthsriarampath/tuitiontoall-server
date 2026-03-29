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

import edu.vinu.request.CreateVacancyRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.service.common.TeacherVacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
