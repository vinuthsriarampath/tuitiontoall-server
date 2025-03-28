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

package edu.vinu.service.common;

import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;

import java.util.List;

public interface UserService {
    User getUserByEmail(String email);
    boolean isUserExist(String email);

    List<User> getAllUsersByFirstNameLike(String firstname);
    List<Student> getAllStudentsByFirstNameLike(String firstName);
    List<Teacher> getAllTeachersByFirsNameLike(String lastName);
}
