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

import edu.vinu.entity.RoleEntity;
import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.entity.user_entities.StudentEntity;
import edu.vinu.entity.user_entities.TeacherEntity;
import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.exception.custom.*;
import edu.vinu.model.user_models.Institute;
import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.*;
import edu.vinu.request.UserLoginRequest;
import edu.vinu.request.registration.InstituteRegistrationRequest;
import edu.vinu.request.registration.StudentRegistrationRequest;
import edu.vinu.request.registration.TeacherRegistrationRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.AuthResponse;
import edu.vinu.service.auth.UserAuthenticationService;
import edu.vinu.service.common.EmailService;
import edu.vinu.service.common.UserService;
import edu.vinu.validator.UserValidator;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static edu.vinu.validator.UserValidator.*;

@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final @Nullable EmailService emailService;
    private final RoleRepository roleRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final InstituteRepository instituteRepository;

    @Value("${app.reset.frontend-reset-url}")
    String resetUrl;

    @Override
    @Transactional
    public Institute registerInstitute(InstituteRegistrationRequest request) {
        validateUser(request.getEmail(), request.getPassword());

        UserEntity userEntity = mapper.map(request, UserEntity.class);
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity.setUserSlug(userService.generateUserSlug(request.getInstituteName()));

        RoleEntity instituteRole = roleRepository.findByRole("institute")
                .orElseThrow(() -> new InternalServerErrorException("Role doesn't exist"));
        userEntity.setRole(instituteRole);
        userEntity.setInstitute(null);

        try {
            UserEntity savedUser = userRepository.save(userEntity);

            InstituteEntity instituteEntity = mapper.map(request, InstituteEntity.class);
            instituteEntity.setUser(savedUser);

            InstituteEntity savedInstitute = instituteRepository.save(instituteEntity);

            if (emailService != null) {
                emailService.SendRegistrationSuccessEmail(savedInstitute.getUser().getEmail(), savedInstitute.getUser().getUserSlug(), savedInstitute.getUser().getRole().getRole());
            }
            return mapper.map(savedInstitute, Institute.class);
        } catch (Exception e) {
            throw new InternalServerErrorException("Registration Failed Due to Internal Error: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public Student registerStudent(StudentRegistrationRequest request) {
        validateUser(request.getEmail(), request.getPassword(), request.getDob());

        UserEntity userEntity = mapper.map(request, UserEntity.class);

        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity.setUserSlug(
                userService.generateUserSlug(
                        request.getFirstName() + "-" + request.getLastName()
                ));

        RoleEntity studentRole = roleRepository.findByRole("student")
                .orElseThrow(() -> new InternalServerErrorException("Role doesn't exist"));
        userEntity.setRole(studentRole);
        userEntity.setStudent(null);

        try {
            UserEntity savedUserEntity = userRepository.save(userEntity);

            StudentEntity studentEntity = mapper.map(request, StudentEntity.class);
            studentEntity.setUser(savedUserEntity);

            StudentEntity savedStudent = studentRepository.save(studentEntity);

            if (emailService != null) {
                emailService.SendRegistrationSuccessEmail(savedStudent.getUser().getEmail(), savedStudent.getUser().getUserSlug(), savedStudent.getUser().getRole().getRole());
            }
            return mapper.map(savedStudent, Student.class);
        } catch (Exception e) {
            throw new InternalServerErrorException("Registration Failed Due to Internal Error");
        }

    }

    @Override
    @Transactional
    public Teacher registerTeacher(TeacherRegistrationRequest request) {
        validateUser(request.getEmail(), request.getPassword(), request.getDob());

        UserEntity userEntity = mapper.map(request, UserEntity.class);

        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity.setUserSlug(
                userService.generateUserSlug(
                        request.getFirstName() + "-" + request.getLastName()
                ));

        RoleEntity teacherRole = roleRepository.findByRole("teacher")
                .orElseThrow(() -> new InternalServerErrorException("Role doesn't exist"));
        userEntity.setRole(teacherRole);
        userEntity.setTeacher(null);
        try {
            UserEntity savedUserEntity = userRepository.save(userEntity);

            TeacherEntity teacherEntity = mapper.map(request, TeacherEntity.class);
            teacherEntity.setUser(savedUserEntity);
            TeacherEntity savedTeacher = teacherRepository.save(teacherEntity);

            if (emailService != null) {
                emailService.SendRegistrationSuccessEmail(savedTeacher.getUser().getEmail(), savedTeacher.getUser().getUserSlug(), savedTeacher.getUser().getRole().getRole());
            }
            return mapper.map(savedTeacher, Teacher.class);
        } catch (Exception e) {
            throw new InternalServerErrorException("Registration Failed Due to Internal Error");
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
    public AuthResponse verify(UserLoginRequest request) {
        try {
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

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authentication);
                UserEntity userEntity = userRepository.findByEmail(request.getEmail());
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

                return new AuthResponse(token,user);
            }
            throw new UnauthorizedException("Invalid access");
        } catch (AuthenticationException e) {
            log.error("UserAuthenticationServiceImpl Section 1: {}", e.getMessage());
            throw new UnauthorizedException(e.getMessage());
        } catch (NullPointerException | IndexOutOfBoundsException | BeansException e) {
            log.error("UserAuthenticationServiceImpl Section 2: {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        } catch (RuntimeException e) {
            log.error("RuntimeException in UserAuthenticationServiceImpl: {}", e.getMessage());
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public ApiResponse startForgotPassword(String email) {
        try {
            User user = userService.getUserByEmail(email);
            String resetToken = jwtService.generateResetToken(email);
            String link = resetUrl + resetToken;
            String name = "";
            switch (user.getRole().getRole()) {
                case "institute" -> {
                    InstituteEntity instituteEntity = instituteRepository.findInstituteByEmail(email).
                            orElseThrow(() -> new NotFoundException("Institute Not found!"));
                    name = instituteEntity.getInstituteName();
                }
                case "teacher" -> {
                    TeacherEntity teacherEntity = teacherRepository.findTeacherByEmail(email)
                            .orElseThrow(() -> new NotFoundException("Teacher Not Found!"));
                    name = teacherEntity.getFirstName() + " " + teacherEntity.getLastName();
                }
                case "student" -> {
                    StudentEntity studentEntity = studentRepository.findStudentByEmail(email)
                            .orElseThrow(() -> new NotFoundException("Student Not Found!"));
                    name = studentEntity.getFirstName() + " " + studentEntity.getLastName();
                }
            }
            assert emailService != null;
            emailService.SendPasswordResetEmail(email, name, link);
            return new ApiResponse("Reset token generated successfully Please check your email for the reset link.", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ApiResponse resetPassword(String token, String newPassword) {
        if (!jwtService.validateResetToken(token)) {
            throw new InvalidInputException("Invalid or expired reset token");
        }
        if (jwtService.validateResetToken(token)) {
            String email = jwtService.extractUsername(token);
            UserEntity userEntity = userRepository.findByEmail(email);

            if (!UserValidator.isValidatePassword(newPassword)) {
                throw new InvalidInputException("Password does not meet security requirements.");
            }
            userEntity.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(userEntity);
            log.info("Password reset successful for user: {}", email);
            return new ApiResponse("Password reset successfully", null);
        } else {
            throw new InvalidInputException("Invalid reset token");
        }
    }

    @Override
    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
