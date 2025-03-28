/*
 * Copyright (c) 2025 vinuth sri arampath
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

package edu.vinu.authentication;

import edu.vinu.model.user_models.User;
import edu.vinu.request.UserLoginRequest;
import edu.vinu.request.registration.InstituteRegistrationRequest;
import edu.vinu.request.registration.StudentRegistrationRequest;
import edu.vinu.request.registration.TeacherRegistrationRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.AuthResponse;
import edu.vinu.service.auth.UserAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static edu.vinu.validator.UserValidator.USER_VALIDATION_FAILED_ERROR;

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
        User savedInstitute = userAuthService.registerInstitute(institute);
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
        User savedTeacher = userAuthService.registerTeacher(teacher);
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
        User savedStudent = userAuthService.registerStudent(student);
        return ResponseEntity.ok(new ApiResponse("Student Registered Successfully!", savedStudent));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest){
        AuthResponse authResponse = userAuthService.verify(userLoginRequest);
        return ResponseEntity.ok(authResponse);
    }
}
