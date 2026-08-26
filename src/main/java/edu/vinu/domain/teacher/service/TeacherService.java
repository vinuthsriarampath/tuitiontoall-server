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

package edu.vinu.domain.teacher.service;

import edu.vinu.domain.teacher.dtos.request.TeacherDetailsUpdateRequest;
import edu.vinu.domain.teacher.dtos.response.Teacher;
import edu.vinu.domain.user.dto.User;

import java.util.List;

public interface TeacherService {
    boolean isTeacherExistById(Long teacherId);

    List<Teacher> getAllTeachers();

    List<User> getAllTeachersByFirstName(String lastName);

    Teacher updateTeacherDetails(String email, TeacherDetailsUpdateRequest teacherDetailsUpdateRequest);
}
