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

package edu.vinu.domain.student.service.impl;

import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.student.dto.request.StudentDetailsUpdateRequest;
import edu.vinu.domain.student.dto.response.Student;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.student.mapper.StudentMapper;
import edu.vinu.domain.student.repository.StudentRepository;
import edu.vinu.domain.student.service.StudentService;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.mapper.UserMapper;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.vinu.domain.user.validator.UserValidator.isValidDob;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserService userService;

    @Override
    public List<Student> getAllStudents() {
        List<Student> studentList = studentRepository.getAllStudents()
                .stream()
                .map(StudentMapper::toStudent)
                .toList();
        if (studentList.isEmpty()){
            throw new NotFoundException("No Students Found");
        }
        return studentList;
    }

    @Override
    public List<User> getAllStudentsByFirstName(String firstName) {
        return studentRepository.getStudentsByFirstNameLike(firstName).stream()
                .map(studentEntity -> UserMapper.toUser(studentEntity.getUser(), StudentMapper.toStudent(studentEntity)))
                .toList();
    }

    @Override
    public Student updateStudentDetails(String email, StudentDetailsUpdateRequest studentDetailsUpdateRequest) {
        if (!isValidDob(studentDetailsUpdateRequest.getDob())){
            throw new InvalidInputException("You must be at least 6 years old");
        }

        UserEntity userEntity = userService.getUserEntityByEmail(email);
        userService.updateUserDetails(email, studentDetailsUpdateRequest);

        StudentEntity studentEntity = userEntity.getStudent();

        studentEntity.setFirstName(studentDetailsUpdateRequest.getFirstName());
        studentEntity.setLastName(studentDetailsUpdateRequest.getLastName());
        studentEntity.setDob(studentDetailsUpdateRequest.getDob());


        return StudentMapper.toStudent(studentRepository.save(studentEntity));
    }
}
