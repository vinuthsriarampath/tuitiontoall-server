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

package edu.vinu.domain.teacher.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.teacher.dtos.request.TeacherDetailsUpdateRequest;
import edu.vinu.domain.teacher.dtos.response.Teacher;
import edu.vinu.domain.teacher.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v2/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PreAuthorize("hasAuthority('teacher')")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse> updateTeacherDetails(@Valid @RequestBody TeacherDetailsUpdateRequest teacherDetailsUpdateRequest){
        Teacher updatedTeacherDetails = teacherService.updateTeacherDetails(SecurityContextHolder.getContext().getAuthentication().getName(),teacherDetailsUpdateRequest);
        return ResponseEntity.status(OK).body(new ApiResponse("Teacher Profile Updated!",updatedTeacherDetails));
    }

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/validate/role")
    public ResponseEntity<ApiResponse> validateTeacher(){
        return ResponseEntity.status(OK).body(new ApiResponse("User has teacher role!", null));
    }
}
