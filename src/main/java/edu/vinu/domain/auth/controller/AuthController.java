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
import edu.vinu.domain.user.dto.Student;
import edu.vinu.domain.teacher.dtos.response.Teacher;
import edu.vinu.domain.institute.request.InstituteRegistrationRequest;
import edu.vinu.domain.user.request.registration.StudentRegistrationRequest;
import edu.vinu.domain.teacher.dtos.request.TeacherRegistrationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static edu.vinu.domain.user.validator.UserValidator.USER_VALIDATION_FAILED_ERROR;

@CrossOrigin
@RestController
@RequestMapping("api/v2/auth")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthController {
    private final UserAuthenticationService userAuthService;

    @PostMapping("/register/institute")
    public ResponseEntity<ApiResponse> registerInstitute(@Valid @RequestBody InstituteRegistrationRequest institute, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponse(USER_VALIDATION_FAILED_ERROR, errors));
        }
        Institute savedInstitute = userAuthService.registerInstitute(institute);
        return ResponseEntity.ok(new ApiResponse("Institute Registered Successfully!", savedInstitute));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<ApiResponse> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponse(USER_VALIDATION_FAILED_ERROR, errors));
        }
        Teacher savedTeacher = userAuthService.registerTeacher(teacher);
        return ResponseEntity.ok(new ApiResponse("Teacher Registered Successfully!", savedTeacher));
    }

    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse> registerStudent(@Valid @RequestBody StudentRegistrationRequest student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponse(USER_VALIDATION_FAILED_ERROR, errors));
        }
        Student savedStudent = userAuthService.registerStudent(student);
        return ResponseEntity.ok(new ApiResponse("Student Registered Successfully!", savedStudent));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = userAuthService.verify(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/forgot-password/request")
    public ResponseEntity<ApiResponse> startForgotPassword(@RequestBody String email){
        ApiResponse apiResponse = userAuthService.startForgotPassword(email);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String token, @RequestBody String newPassword){
        ApiResponse apiResponse = userAuthService.resetPassword(token, newPassword);
        return ResponseEntity.ok(apiResponse);
    }
}
