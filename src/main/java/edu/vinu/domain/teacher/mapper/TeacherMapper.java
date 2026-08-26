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

package edu.vinu.domain.teacher.mapper;

import edu.vinu.domain.teacher.dtos.response.Teacher;
import edu.vinu.domain.teacher.entity.TeacherEntity;

public class TeacherMapper {

    public static Teacher toTeacher(TeacherEntity teacherEntity){
        return Teacher.builder()
                .id(teacherEntity.getId())
                .firstName(teacherEntity.getFirstName())
                .lastName(teacherEntity.getLastName())
                .dob(teacherEntity.getDob())
                .build();
    }
}
