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

import edu.vinu.domain.student.mapper.StudentMapper;
import edu.vinu.domain.student.repository.StudentRepository;
import edu.vinu.domain.student.service.StudentService;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public List<User> getAllStudentsByFirstName(String firstName) {
        return studentRepository.getStudentsByFirstNameLike(firstName).stream()
                .map(studentEntity -> UserMapper.toUser(studentEntity.getUser(), StudentMapper.toStudent(studentEntity)))
                .toList();
    }
}
