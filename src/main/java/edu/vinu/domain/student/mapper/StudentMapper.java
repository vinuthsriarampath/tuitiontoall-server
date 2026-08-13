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

package edu.vinu.domain.student.mapper;

import edu.vinu.domain.user.dto.Student;
import edu.vinu.domain.student.entity.StudentEntity;

public class StudentMapper {
    public static Student toStudent(StudentEntity studentEntity){
        return Student.builder()
                .id(studentEntity.getId())
                .firstName(studentEntity.getFirstName())
                .lastName(studentEntity.getLastName())
                .dob(studentEntity.getDob())
                .build();
    }
}
