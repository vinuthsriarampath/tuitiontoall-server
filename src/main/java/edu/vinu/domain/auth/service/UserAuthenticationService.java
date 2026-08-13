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

package edu.vinu.domain.auth.service;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.auth.request.LoginRequest;
import edu.vinu.domain.auth.response.AuthResponse;
import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.domain.user.dto.Student;
import edu.vinu.domain.teacher.dtos.response.Teacher;
import edu.vinu.domain.institute.request.InstituteRegistrationRequest;
import edu.vinu.domain.user.request.registration.StudentRegistrationRequest;
import edu.vinu.domain.teacher.dtos.request.TeacherRegistrationRequest;

public interface UserAuthenticationService {
    Institute registerInstitute(InstituteRegistrationRequest user);

    Student registerStudent(StudentRegistrationRequest request);

    Teacher registerTeacher(TeacherRegistrationRequest request);

    AuthResponse verify(LoginRequest request);

    ApiResponse startForgotPassword(String email);

    ApiResponse resetPassword(String token, String newPassword);

    String getCurrentUserEmail();
}
