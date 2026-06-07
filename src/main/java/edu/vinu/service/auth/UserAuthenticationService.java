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

package edu.vinu.service.auth;

import edu.vinu.model.user_models.Institute;
import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.domain.request.LoginRequest;
import edu.vinu.request.registration.InstituteRegistrationRequest;
import edu.vinu.request.registration.StudentRegistrationRequest;
import edu.vinu.request.registration.TeacherRegistrationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.response.AuthResponse;

public interface UserAuthenticationService {
    Institute registerInstitute(InstituteRegistrationRequest user);

    Student registerStudent(StudentRegistrationRequest request);

    Teacher registerTeacher(TeacherRegistrationRequest request);

    AuthResponse verify(LoginRequest request);

    ApiResponse startForgotPassword(String email);

    ApiResponse resetPassword(String token, String newPassword);

    String getCurrentUserEmail();
}
