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
import edu.vinu.exception.custom.UserNotFoundException;
import edu.vinu.model.user_models.Institute;
import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserServiceImpl implements UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity=userRepository.findByEmail(email);
        if (userEntity == null){
            throw new UserNotFoundException("A user from "+email+" not found!!");
        }
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

    @Override
    public List<User> getAllUsersByFirstNameLike(String firstname) {
        List<User> userList =new ArrayList<>();
        userList.addAll(getAllStudentsByFirstNameLike(firstname));
        userList.addAll(getAllTeachersByFirsNameLike(firstname));
        if (userList.isEmpty()){
            throw new UserNotFoundException("There are no users starts with "+firstname);
        }
        return userList;
    }

    @Override
    public List<Student> getAllStudentsByFirstNameLike(String firstName) {
        return userRepository.getStudentsByFirstNameLike(firstName).stream()
                .map(this::convertToStudentModel)
                .toList();
    }

    @Override
    public List<Teacher> getAllTeachersByFirsNameLike(String firstName) {
        return userRepository.getTeachersByFirstNameLike(firstName).stream()
                .map(this::convertToTeacherModel)
                .toList();
    }

    public User convertToUserModel(UserEntity userEntity){
        return mapper.map(userEntity, User.class);
    }

    public Institute convertToInstituteModel(InstituteEntity instituteEntity){
        return mapper.map(instituteEntity, Institute.class);
    }

    public Student convertToStudentModel(StudentEntity studentEntity){
        return mapper.map(studentEntity, Student.class);
    }

    public Teacher convertToTeacherModel(TeacherEntity teacherEntity){
        return mapper.map(teacherEntity, Teacher.class);
    }
}
