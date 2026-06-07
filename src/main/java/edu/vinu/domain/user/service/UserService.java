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

package edu.vinu.domain.user.service;

import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.model.user_models.Institute;
import edu.vinu.domain.user.dto.Student;
import edu.vinu.domain.user.dto.Teacher;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.institute.request.InstituteDetailsUpdateRequest;
import edu.vinu.domain.user.request.update.StudentDetailsUpdateRequest;
import edu.vinu.domain.user.request.update.TeacherDetailsUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    User getUserByEmail(String email);

    UserEntity getUserEntityByEmail(String email);

    boolean isUserExist(String email);

    boolean isTeacherExistByTeacherId(Long teacherId);

    List<Object> getAllUsersByFirstNameLike(String firstname);

    List<User> getAllStudentsByFirstNameLike(String firstName);

    List<User> getAllTeachersByFirstNameLike(String lastName);

    List<Student> getAllStudents();

    List<Teacher> getAllTeachers();

    List<Institute> getAllInstitutes();

    List<User> getAllInstitutesByInstituteName(String instituteName);

    Institute updateInstituteDetails(String currentEmail, InstituteDetailsUpdateRequest instituteDetailsUpdateRequest);

    String generateUserSlug(String base);

    Teacher updateTeacherDetails(String email, TeacherDetailsUpdateRequest teacherDetailsUpdateRequest);

    Student updateStudentDetails(String email, @Valid StudentDetailsUpdateRequest studentDetailsUpdateRequest);

    void disableUserAccountByEmail(String email);

    boolean isUserDisabled(UserEntity userEntity);

    User getUserByUserSlug(String userSlug);
}
