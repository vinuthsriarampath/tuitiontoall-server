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

package edu.vinu.service.auth.impl;

import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.entity.user_entities.StudentEntity;
import edu.vinu.entity.user_entities.TeacherEntity;
import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.enums.Role;
import edu.vinu.exception.custom.InternalServerErrorException;
import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.exception.custom.UnauthorizedException;
import edu.vinu.exception.custom.UserAlreadyExistException;
import edu.vinu.model.user_models.Institute;
import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.request.UserLoginRequest;
import edu.vinu.request.registration.InstituteRegistrationRequest;
import edu.vinu.request.registration.StudentRegistrationRequest;
import edu.vinu.request.registration.TeacherRegistrationRequest;
import edu.vinu.request.registration.UserRegistrationRequest;
import edu.vinu.response.AuthResponse;
import edu.vinu.service.auth.UserAuthenticationService;
import edu.vinu.service.common.UserService;
import edu.vinu.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static edu.vinu.validator.UserValidator.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Institute registerInstitute(InstituteRegistrationRequest request) {
        validateUser(request.getEmail(), request.getPassword());
        return saveUser(request, InstituteEntity.class, Institute.class, Role.ROLE_INSTITUTE);
    }

    @Override
    public Student registerStudent(StudentRegistrationRequest request) {
        validateUser(request.getEmail(), request.getPassword(), request.getDob());
        return saveUser(request, StudentEntity.class, Student.class, Role.ROLE_STUDENT);
    }

    @Override
    public Teacher registerTeacher(TeacherRegistrationRequest request) {
        validateUser(request.getEmail(), request.getPassword(), request.getDob());
        return saveUser(request, TeacherEntity.class, Teacher.class, Role.ROLE_TEACHER);
    }

    private <T, U extends User> U saveUser(T request, Class<? extends UserEntity> entityClass, Class<U> modelClass, Role role) {
        if (request instanceof UserRegistrationRequest userRequest) {
            userRequest.setPassword(encoder.encode(userRequest.getPassword()));
            UserEntity userEntity = mapper.map(userRequest, entityClass);
            userEntity.setRole(role);

            try {
                UserEntity savedEntity = userRepository.save(userEntity);
                return mapper.map(savedEntity, modelClass);
            } catch (Exception e) {
                throw new InternalServerErrorException("Registration Failed Due to Internal Error");
            }
        } else {
            throw new IllegalArgumentException("Invalid request type");
        }
    }

    private void validateUser(String email, String password) {
        if (!isValidateEmail(email)) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (!isValidatePassword(password)) {
            throw new InvalidInputException("Password does not meet security requirements.");
        }
        if (userService.isUserExist(email)) {
            throw new UserAlreadyExistException();
        }
    }

    private void validateUser(String email, String password, LocalDate dob) {
        if (!isValidateEmail(email)) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (!isValidatePassword(password)) {
            throw new InvalidInputException("Password does not meet security requirements.");
        }
        if (!isValidDob(dob)) {
            throw new InvalidInputException("You must be at least 6 years old");
        }
        if (userService.isUserExist(email)) {
            throw new UserAlreadyExistException();
        }
    }

    @Override
    public AuthResponse verify(UserLoginRequest request){
        if (!UserValidator.isValidateEmail(request.getEmail())) {
            throw new InvalidInputException("Invalid email");
        }

        Authentication authentication = authManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        if (authentication.isAuthenticated()){
            String token = jwtService.generateToken(authentication);
            UserEntity userEntity = userRepository.findByEmail(request.getEmail());

            if (userEntity instanceof InstituteEntity) {
                return new AuthResponse(token, mapper.map(userEntity, Institute.class));
            } else if (userEntity instanceof StudentEntity) {
                return new AuthResponse(token, mapper.map(userEntity, Student.class));
            } else if (userEntity instanceof TeacherEntity) {
                return new AuthResponse(token, mapper.map(userEntity, Teacher.class));
            } else {
                return new AuthResponse(token, mapper.map(userEntity, User.class));
            }
        }
        throw new UnauthorizedException("Invalid access");
    }
}
