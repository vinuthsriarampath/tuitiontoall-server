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

package edu.vinu.domain.teacher.service.impl;

import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.teacher.dtos.response.Teacher;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.teacher.entity.TeacherEntity;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.teacher.mapper.TeacherMapper;
import edu.vinu.domain.user.mapper.UserMapper;
import edu.vinu.domain.teacher.repository.TeacherRepository;
import edu.vinu.domain.teacher.dtos.request.TeacherDetailsUpdateRequest;
import edu.vinu.domain.teacher.service.TeacherService;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static edu.vinu.domain.user.validator.UserValidator.isValidDob;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final UserService userService;
    private final TeacherRepository teacherRepository;

    @Override
    public List<Teacher> getAllTeachers() {
        List<Teacher> teacherList = teacherRepository.getAllTeachers().stream()
                .map(TeacherMapper::toTeacher)
                .toList();
        if (teacherList.isEmpty()){
            throw new NotFoundException("No Teachers Found!");
        }
        return teacherList;
    }

    @Override
    public boolean isTeacherExistById(Long teacherId) {
        return teacherRepository.existsById(teacherId);
    }

    @Override
    public List<User> getAllTeachersByFirstName(String firstName) {
        return teacherRepository.getTeachersByFirstNameLike(firstName).stream()
                .map(teacherEntity -> UserMapper.toUser(teacherEntity.getUser(), TeacherMapper.toTeacher(teacherEntity)))
                .toList();
    }

    @Transactional
    @Override
    public Teacher updateTeacherDetails(String email, TeacherDetailsUpdateRequest teacherDetailsUpdateRequest) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);
        if (!isValidDob(teacherDetailsUpdateRequest.getDob())) {
            throw new InvalidInputException("You must be at least 6 years old");
        }

        userService.updateUserDetails(email, teacherDetailsUpdateRequest);

        TeacherEntity teacherEntity = userEntity.getTeacher();

        teacherEntity.setFirstName(teacherDetailsUpdateRequest.getFirstName());
        teacherEntity.setLastName(teacherDetailsUpdateRequest.getLastName());
        teacherEntity.setDob(teacherDetailsUpdateRequest.getDob());

        return TeacherMapper.toTeacher(teacherRepository.save(teacherEntity));
    }
}
