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

package edu.vinu.domain.auth.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.auth.request.LoginRequest;
import edu.vinu.domain.auth.response.AuthResponse;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.domain.institute.request.InstituteRegistrationRequest;
import edu.vinu.domain.student.dto.request.StudentRegistrationRequest;
import edu.vinu.domain.student.dto.response.Student;
import edu.vinu.domain.teacher.dtos.request.TeacherRegistrationRequest;
import edu.vinu.domain.teacher.dtos.response.Teacher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@CrossOrigin
@RestController
@RequestMapping("api/v2/auth")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthController {
    private final UserAuthenticationService userAuthService;

    @PostMapping("/register/institute")
    public ResponseEntity<ApiResponse<Institute>> registerInstitute(@Valid @RequestBody InstituteRegistrationRequest institute) {
        return ResponseEntity.ok(createApiResponse("Institute Registered Successfully!", userAuthService.registerInstitute(institute)));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<ApiResponse<Teacher>> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest teacher) {
        return ResponseEntity.ok(createApiResponse("Teacher Registered Successfully!", userAuthService.registerTeacher(teacher)));
    }

    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse<Student>> registerStudent(@Valid @RequestBody StudentRegistrationRequest student) {
        return ResponseEntity.ok(createApiResponse("Student Registered Successfully!", userAuthService.registerStudent(student)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userAuthService.verify(loginRequest));
    }

    @PostMapping("/forgot-password/request")
    public ResponseEntity<ApiResponse<Void>> startForgotPassword(@RequestBody String email){
        return ResponseEntity.ok(userAuthService.startForgotPassword(email));
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestParam String token, @RequestBody String newPassword){
        return ResponseEntity.ok(userAuthService.resetPassword(token, newPassword));
    }
}
