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
import edu.vinu.enums.Role;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.UserNotFoundException;
import edu.vinu.model.user_models.Institute;
import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.request.update_user_details.InstituteDetailsUpdateRequest;
import edu.vinu.request.update_user_details.TeacherDetailsUpdateRequest;
import edu.vinu.service.common.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.vinu.validator.UserValidator.isValidDob;

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
        return convertToModel(userEntity);
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

    @Override
    public List<Student> getAllStudents() {
        List<Student> studentList = userRepository.findAllByRole(Role.ROLE_STUDENT)
                .stream()
                .map(studentEntity -> (Student) convertToModel(studentEntity))
                .toList();
        if (studentList.isEmpty()){
            throw new UserNotFoundException("No Students Found");
        }
        return studentList;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        List<Teacher> teacherList = userRepository.findAllByRole(Role.ROLE_TEACHER)
                .stream()
                .map(teacherEntity -> (Teacher) convertToModel(teacherEntity))
                .toList();
        if (teacherList.isEmpty()){
            throw new UserNotFoundException("No Teachers Found!");
        }
        return teacherList;
    }

    @Override
    public List<Institute> getAllInstitutes() {
        List<Institute> instituteList =  userRepository.findAllByRole(Role.ROLE_INSTITUTE)
                .stream()
                .map(instituteEntity -> (Institute) convertToModel(instituteEntity))
                .toList();
        if (instituteList.isEmpty()){
            throw new UserNotFoundException("No Institutes Found!");
        }
        return instituteList;
    }

    @Override
    public List<Institute> getAllInstitutesByInstituteName(String instituteName) {
        return userRepository.findByInstituteName(instituteName)
                .stream()
                .map(this::convertToInstituteModel)
                .toList();
    }

    @Override
    public Institute updateInstituteDetails(String email, InstituteDetailsUpdateRequest instituteDetailsUpdateRequest) {
        if (!isUserExist(email)) {
            throw new UserNotFoundException("User not found for " + email);
        }
        return Optional.ofNullable(userRepository.findByEmail(email))
                .filter(InstituteEntity.class::isInstance)
                .map(userEntity -> {
                    InstituteEntity instituteEntity = (InstituteEntity) userEntity;
                    instituteEntity.setAddress(instituteDetailsUpdateRequest.getAddress());
                    instituteEntity.setContact(instituteDetailsUpdateRequest.getContact());
                    instituteEntity.setInstituteName(instituteDetailsUpdateRequest.getInstituteName());

                    return convertToInstituteModel(userRepository.save(instituteEntity));
                })
                .orElseThrow(() -> new UserNotFoundException("Institute not found for " + email));
    }

    @Override
    public Teacher updateTeacherDetails(String email, TeacherDetailsUpdateRequest teacherDetailsUpdateRequest) {
        if (!isUserExist(email)){
            throw new UserNotFoundException("No User Found By "+email);
        }
        if (!isValidDob(teacherDetailsUpdateRequest.getDob())) {
            throw new InvalidInputException("You must be at least 6 years old");
        }
        return Optional.ofNullable(userRepository.findByEmail(email))
                .filter(TeacherEntity.class::isInstance)
                .map(userEntity -> {
                    TeacherEntity teacherEntity = (TeacherEntity) userEntity;
                    teacherEntity.setContact(teacherDetailsUpdateRequest.getContact());
                    teacherEntity.setAddress(teacherDetailsUpdateRequest.getAddress());
                    teacherEntity.setFirstName(teacherDetailsUpdateRequest.getFirstName());
                    teacherEntity.setLastName(teacherDetailsUpdateRequest.getLastName());
                    teacherEntity.setDob(teacherDetailsUpdateRequest.getDob());

                    return convertToTeacherModel(userRepository.save(teacherEntity));
                })
                .orElseThrow(() -> new UserNotFoundException("No teacher found by "+email));
    }

    public User convertToModel(UserEntity userEntity){
        if (userEntity instanceof StudentEntity){
            return mapper.map(userEntity, Student.class);
        }else if (userEntity instanceof TeacherEntity){
            return mapper.map(userEntity, Teacher.class);
        }else if (userEntity instanceof InstituteEntity){
            return mapper.map(userEntity, Institute.class);
        }else {
            return mapper.map(userEntity, User.class);
        }
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
