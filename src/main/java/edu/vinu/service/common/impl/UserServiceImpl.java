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

package edu.vinu.service.common.impl;

import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.entity.user_entities.StudentEntity;
import edu.vinu.entity.user_entities.TeacherEntity;
import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.model.user_models.Institute;
import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserServiceImpl implements UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity=userRepository.findByEmail(email);
        if (userEntity instanceof InstituteEntity) {
            return mapper.map(userEntity, Institute.class);
        } else if (userEntity instanceof StudentEntity) {
            return mapper.map(userEntity, Student.class);
        } else if (userEntity instanceof TeacherEntity) {
            return mapper.map(userEntity, Teacher.class);
        } else {
            return mapper.map(userEntity, User.class);
        }
    }

    @Override
    public boolean isUserExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
