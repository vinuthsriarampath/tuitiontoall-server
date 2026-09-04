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

package edu.vinu.domain.student.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.student.dto.request.StudentDetailsUpdateRequest;
import edu.vinu.domain.student.dto.response.Student;
import edu.vinu.domain.student.service.StudentLearningService;
import edu.vinu.domain.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v2/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final StudentLearningService studentLearningService;

    @PreAuthorize("hasAuthority('student')")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse> updateStudentDetails(@Valid @RequestBody StudentDetailsUpdateRequest studentDetailsUpdateRequest){
        Student updatedStudentDetails = studentService.updateStudentDetails(SecurityContextHolder.getContext().getAuthentication().getName(),studentDetailsUpdateRequest);
        return ResponseEntity.status(OK).body(new ApiResponse("Student Profile Updated!",updatedStudentDetails));
    }

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/validate/role")
    public ResponseEntity<ApiResponse> validateStudent(){
        return ResponseEntity.status(OK).body(new ApiResponse("User has student role!", null));
    }

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/me/learning")
    public ResponseEntity<ApiResponse> getMyLearningDetails(){
        return ResponseEntity.ok(studentLearningService.getMyLearningDetails());
    }

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/me/learning/courses/{courseId}/enrollment-history")
    public ResponseEntity<ApiResponse> getEnrollmentHistory(@PathVariable Long courseId) {
        return ResponseEntity.ok(studentLearningService.getEnrollmentHistory(courseId));
    }
}
