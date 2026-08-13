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

package edu.vinu.domain.user.service.impl;

import edu.vinu.common.exception.custom.InternalServerErrorException;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.institute.mapper.InstituteMapper;
import edu.vinu.domain.institute.repository.InstituteRepository;
import edu.vinu.domain.institute.request.InstituteDetailsUpdateRequest;
import edu.vinu.domain.user.dto.Student;
import edu.vinu.domain.teacher.dtos.response.Teacher;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.teacher.entity.TeacherEntity;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.mapper.UserMapper;
import edu.vinu.domain.student.repository.StudentRepository;
import edu.vinu.domain.teacher.repository.TeacherRepository;
import edu.vinu.domain.user.repository.UserRepository;
import edu.vinu.domain.student.dto.request.StudentDetailsUpdateRequest;
import edu.vinu.domain.user.request.update.UserDetailsUpdateRequest;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.vinu.domain.user.validator.UserValidator.isValidDob;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserServiceImpl implements UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final InstituteRepository instituteRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity=this.getUserEntityByEmail(email);
        User user = mapper.map(userEntity, User.class);
        switch (user.getRole().getRole()){
            case "student":
                user.setDetails(mapper.map(userEntity.getStudent(), Student.class));
                break;
            case "teacher":
                user.setDetails(mapper.map(userEntity.getTeacher(), Teacher.class));
                break;
            case "institute":
                user.setDetails(mapper.map(userEntity.getInstitute(), Institute.class));
                break;
        }
        return user;
    }

    @Override
    public UserEntity getUserEntityByEmail(String email) {
        UserEntity userEntity=userRepository.findByEmail(email);
        if (userEntity == null){
            throw new NotFoundException("A user from "+email+" not found!!");
        }
        return userEntity;
    }

    @Override
    public UserEntity getUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    @Override
    public boolean isUserExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User updateUserDetails(String email, UserDetailsUpdateRequest userUpdateRequest) {
        UserEntity userEntity = getUserEntityByEmail(email);

        userEntity.setAddress(userUpdateRequest.getAddress());
        userEntity.setContact(userUpdateRequest.getContact());

        return UserMapper.toUser(userRepository.save(userEntity));
    }

    @Override
    public List<Object> getAllUsersByFirstNameLike(String firstname) {
        List<Object> userList =new ArrayList<>();
//        userList.addAll(getAllStudentsByFirstName(firstname));
//        userList.addAll(getAllTeachersByFirstName(firstname));
        if (userList.isEmpty()){
            throw new NotFoundException("There are no users starts with "+firstname);
        }
        return userList;
    }





    @Override
    public List<Institute> getAllInstitutes() {
        List<Institute> instituteList =  instituteRepository.getAllInstitutes()
                .stream()
                .map(this::convertToInstituteModel)
                .toList();

        if (instituteList.isEmpty()){
            throw new NotFoundException("No Institutes Found!");
        }
        return instituteList;
    }

    @Override
    public List<User> getAllInstitutesByInstituteName(String instituteName) {
        return instituteRepository.findByInstituteName(instituteName)
                .stream()
                .map(instituteEntity -> UserMapper.toUser(instituteEntity.getUser(), InstituteMapper.toInstitute(instituteEntity)))
                .toList();
    }

    @Override
    public Institute updateInstituteDetails(String email, InstituteDetailsUpdateRequest instituteDetailsUpdateRequest) {
        if (!isUserExist(email)) {
            throw new NotFoundException("User not found for " + email);
        }
        return Optional.ofNullable(userRepository.findByEmail(email))
                .map(userEntity -> {
                    userEntity.setAddress(instituteDetailsUpdateRequest.getAddress());
                    userEntity.setContact(instituteDetailsUpdateRequest.getContact());

                    userRepository.save(userEntity);

                    userEntity.getInstitute().setInstituteName(instituteDetailsUpdateRequest.getInstituteName());

                    return convertToInstituteModel(instituteRepository.save(userEntity.getInstitute()));
                })
                .orElseThrow(() -> new NotFoundException("Institute not found for " + email));
    }

    @Override
    public String generateUserSlug(String base) {
        String slug = base.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-");
        String uniqueSlug = slug;
        int counter = 1;

        while (userRepository.existsByUserSlug(uniqueSlug)) {
            uniqueSlug = slug + "-" + counter++;
        }

        return uniqueSlug;
    }



    @Override
    public Student updateStudentDetails(String email, StudentDetailsUpdateRequest studentDetailsUpdateRequest) {
        if (!isUserExist(email)){
            throw new NotFoundException("No User Found By "+email);
        }
        if (!isValidDob(studentDetailsUpdateRequest.getDob())){
            throw new InvalidInputException("You must be at least 6 years old");
        }
        return Optional.ofNullable(userRepository.findByEmail(email))
                .map(userEntity -> {
                    userEntity.setAddress(studentDetailsUpdateRequest.getAddress());
                    userEntity.setContact(studentDetailsUpdateRequest.getContact());

                    userRepository.save(userEntity);

                    StudentEntity studentEntity = userEntity.getStudent();

                    studentEntity.setFirstName(studentDetailsUpdateRequest.getFirstName());
                    studentEntity.setLastName(studentDetailsUpdateRequest.getLastName());
                    studentEntity.setDob(studentDetailsUpdateRequest.getDob());

                    return convertToStudentModel(studentRepository.save(studentEntity));
                })
                .orElseThrow(() -> new NotFoundException("No Student found by "+email));
    }

    @Override
    public void disableUserAccountByEmail(String email) {
        Optional.ofNullable(userRepository.findByEmail(email))
                .map(userEntity -> {
                    if (isUserDisabled(userEntity)) {
                        throw new DisabledException("User is disabled Already");
                    }
                    return userEntity;
                })
                .ifPresentOrElse(
                        userRepository::delete,
                        () -> {
                            throw new NotFoundException("User not found by " + email);
                        });
    }

    @Override
    public boolean isUserDisabled(UserEntity userEntity) {
        return userRepository.isUserDisabledByEmail(userEntity.getEmail());
    }

    @Override
    public User getUserByUserSlug(String userSlug) {
        return  Optional.ofNullable(userRepository.findByUserSlug(userSlug))
                .map(userEntity -> {
                    if (isUserDisabled(userEntity)) {
                        throw new DisabledException("User is disabled");
                    }
                    User user = mapper.map(userEntity, User.class);
                    switch (user.getRole().getRole()){
                        case "student":
                            user.setDetails(mapper.map(userEntity.getStudent(), Student.class));
                            break;
                        case "teacher":
                            user.setDetails(mapper.map(userEntity.getTeacher(), Teacher.class));
                            break;
                        case "institute":
                        user.setDetails(mapper.map(userEntity.getInstitute(), Institute.class));
                        break;
                    }
                    return user;
                })
                .orElseThrow(()-> new NotFoundException("No user found by "+userSlug));
    }

    public Institute convertToInstituteModel(InstituteEntity instituteEntity){
        try {
            return mapper.map(instituteEntity, Institute.class);
        } catch (ClassCastException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Student convertToStudentModel(StudentEntity studentEntity){
        try {
            return mapper.map(studentEntity, Student.class);
        } catch (ClassCastException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    public Teacher convertToTeacherModel(TeacherEntity teacherEntity){
        try {
            return mapper.map(teacherEntity, Teacher.class);
        } catch (ClassCastException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
