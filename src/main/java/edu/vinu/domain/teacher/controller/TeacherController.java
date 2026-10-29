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

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.application.request.TeacherApplicationFilterRequest;
import edu.vinu.domain.application.response.TeacherApplicationResponse;
import edu.vinu.domain.application.service.ApplicationService;
import edu.vinu.domain.course.response.TeacherCourseViewResponse;
import edu.vinu.domain.course.service.TeacherCourseService;
import edu.vinu.domain.teacher.dtos.request.TeacherDetailsUpdateRequest;
import edu.vinu.domain.teacher.dtos.response.Teacher;
import edu.vinu.domain.teacher.dtos.response.TeacherBootstrapResponse;
import edu.vinu.domain.teacher.dtos.response.TeachingResponse;
import edu.vinu.domain.teacher.service.TeacherBootstrapService;
import edu.vinu.domain.teacher.service.TeacherService;
import edu.vinu.domain.teacher.service.TeachingService;
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
@RequestMapping("/api/v2/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    private final TeachingService teachingService;
    private final TeacherBootstrapService teacherBootstrapService;
    private final TeacherCourseService teacherCourseService;
    private final ApplicationService applicationService;

    @PreAuthorize("hasAuthority('teacher')")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Teacher>> updateTeacherDetails(@Valid @RequestBody TeacherDetailsUpdateRequest teacherDetailsUpdateRequest) {
        Teacher updatedTeacherDetails = teacherService.updateTeacherDetails(SecurityContextHolder.getContext().getAuthentication().getName(), teacherDetailsUpdateRequest);
        return ResponseEntity.status(OK).body(createApiResponse("Teacher Profile Updated!", updatedTeacherDetails));
    }

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/validate/role")
    public ResponseEntity<ApiResponse<Void>> validateTeacher() {
        return ResponseEntity.status(OK).body(createApiResponse("User has teacher role!", null));
    }

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/me/teachings")
    public ResponseEntity<ApiResponse<List<TeachingResponse>>> getMyTeachingDetails() {
        return ResponseEntity.ok(teachingService.getMyTeachingDetails());
    }

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/me/teachings/courses/{courseId}")
    public ResponseEntity<ApiResponse<TeacherCourseViewResponse>> getTeacherDetailedCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(teacherCourseService.getTeacherDetailedCourse(courseId));
    }

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/me/applications")
    public ResponseEntity<PaginatedApiResponse<TeacherApplicationResponse>> getMyApplications(PaginationRequest pagination, TeacherApplicationFilterRequest filters) {
        return ResponseEntity.ok(applicationService.getCurrentTeacherApplications(pagination, filters));
    }

    @PreAuthorize("hasAuthority('teacher')")
    @GetMapping("/me/bootstrap")
    public ResponseEntity<ApiResponse<TeacherBootstrapResponse>> getTeacherBootstrapData() {
        return ResponseEntity.ok(teacherBootstrapService.getTeacherDashboardStats());
    }
}
