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
import edu.vinu.domain.teacher_vacancy.service.TeacherVacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/institutes")
@RequiredArgsConstructor
public class InstituteController {

    private final TeacherVacancyService vacancyService;

    @GetMapping("/{instituteId}/vacancies")
    public ResponseEntity<ApiResponse> getVacanciesByStatusAndInstituteId(@PathVariable(name = "instituteId") Long instituteId, @RequestParam(defaultValue = "OPEN") String status) {
        return ResponseEntity.status(200).body(new ApiResponse("All Vacancies By provided status",vacancyService.getAllByInstituteIdAndStatus(instituteId,status)));
    }
}
