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

package edu.vinu.service.auth;

import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.entity.user_entities.StudentEntity;
import edu.vinu.entity.user_entities.TeacherEntity;
import edu.vinu.entity.user_entities.UserEntity;
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
import edu.vinu.response.AuthResponse;
import edu.vinu.service.auth.impl.JwtService;
import edu.vinu.service.auth.impl.UserAuthenticationServiceImpl;
import edu.vinu.service.common.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserAuthenticationServiceImpl userAuthenticationService;

    private final InstituteRegistrationRequest instituteRegistrationRequest = new InstituteRegistrationRequest();
    private final StudentRegistrationRequest studentRegistrationRequest = new StudentRegistrationRequest();
    private final TeacherRegistrationRequest teacherRegistrationRequest = new TeacherRegistrationRequest();
    private UserLoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        instituteRegistrationRequest.setInstituteName("Test Institute");
        instituteRegistrationRequest.setAddress("123 Main St");
        instituteRegistrationRequest.setContact("1234567890");
        instituteRegistrationRequest.setEmail("institute@test.com");
        instituteRegistrationRequest.setPassword("Password@123");

        studentRegistrationRequest.setFirstName("John");
        studentRegistrationRequest.setLastName("Doe");
        studentRegistrationRequest.setDob(LocalDate.of(2000, 1, 1));
        studentRegistrationRequest.setAddress("123 Main St");
        studentRegistrationRequest.setContact("1234567890");
        studentRegistrationRequest.setEmail("student@test.com");
        studentRegistrationRequest.setPassword("Password@123");

        teacherRegistrationRequest.setFirstName("Jane");
        teacherRegistrationRequest.setLastName("Doe");
        teacherRegistrationRequest.setDob(LocalDate.of(1980, 1, 1));
        teacherRegistrationRequest.setAddress("123 Main St");
        teacherRegistrationRequest.setContact("1234567890");
        teacherRegistrationRequest.setEmail("teacher@test.com");
        teacherRegistrationRequest.setPassword("Password@123");

        loginRequest = new UserLoginRequest("user@test.com", "Password123");
    }

    //Institute Registration Tests

    // Institute Registration Tests with valid information
//    @Test
//    void testRegisterInstitute_success_withValidInformation(){
//
//        InstituteEntity entity = new InstituteEntity();
//        entity.setInstituteName("Test Institute");
//        entity.setAddress("123 Main St");
//        entity.setContact("1234567890");
//        entity.setEmail("institute@test.com");
//        entity.setPassword("Password@123");
//        entity.setRole(Role.ROLE_INSTITUTE);
//
//        Institute model = new Institute();
//        model.setInstituteName("Test Institute");
//        model.setAddress("123 Main St");
//        model.setContact("1234567890");
//        model.setEmail("institute@test.com");
//        model.setPassword("Password@123");
//        model.setRole(Role.ROLE_INSTITUTE);
//
//        String expectedUserSlug = "test-institute";
//
//        entity.setUserSlug(expectedUserSlug);
//        model.setUserSlug(expectedUserSlug);
//
//        when(mapper.map(instituteRegistrationRequest, InstituteEntity.class)).thenReturn(entity);
//
//        when(userRepository.save(entity)).thenReturn(entity);
//
//        when(mapper.map(entity, Institute.class)).thenReturn(model);
//
//        when(userService.isUserExist("institute@test.com")).thenReturn(false);
//
//        when(userService.generateUserSlug("Test Institute")).thenReturn(expectedUserSlug);
//
//        Institute result = userAuthenticationService.registerInstitute(instituteRegistrationRequest);
//
//        assertNotNull(result);
//        assertEquals("Test Institute", result.getInstituteName());
//        assertEquals(expectedUserSlug, result.getUserSlug());
//        verify(userRepository).save(entity);
//        verify(userService).generateUserSlug("Test Institute");
//        verify(encoder).encode("Password@123");
//
//    }

    @Test
    void testRegisterInstitute_invalidEmail_throwsInvalidInputException(){
        // Email does not contain '@' symbol
        instituteRegistrationRequest.setEmail("invalidemailemail.com");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Email contain '@' symbol but docent contain domain name
        instituteRegistrationRequest.setEmail("invalidemail@");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);
    }

    @Test
    void testRegisterInstitute_invalidPassword_throwsInvalidInputException(){
        // Password does not meet 8-character lengths
        instituteRegistrationRequest.setPassword("pass");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);
        
        // Password meets an 8-character length but does not contain at least 1 uppercase character
        instituteRegistrationRequest.setPassword("password");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Password meets an 8-character length, at least 1 uppercase character but does not contain at least 1 lower character
        instituteRegistrationRequest.setPassword("PASSWORD");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Password meets 8-character length, at least 1 uppercase character and lowercase character but does not contain a special character
        instituteRegistrationRequest.setPassword("Password");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Password meets 8-character length contain uppercase character, a special character but does not contain the at least numeric character
        instituteRegistrationRequest.setPassword("Password@");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

    }

    @Test
    void registerInstitute_userExists_throwsUserAlreadyExistException() {
        when(userService.isUserExist("institute@test.com")).thenReturn(true);
        assertThrows(UserAlreadyExistException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);
    }

//    @Test
//    void registerInstitute_saveFails_throwsInternalServerErrorException() {
//        InstituteEntity entity = new InstituteEntity();
//        entity.setInstituteName("Test Institute");
//        entity.setEmail("institute@test.com");
//
//        when(encoder.encode(instituteRegistrationRequest.getPassword())).thenReturn("encodedPassword");
//        when(mapper.map(instituteRegistrationRequest, InstituteEntity.class)).thenReturn(entity);
//        when(userService.generateUserSlug("Test Institute")).thenReturn("test-institute");
//        when(userRepository.save(entity)).thenThrow(new RuntimeException("DB Error"));
//        when(userService.isUserExist("institute@test.com")).thenReturn(false);
//
//        assertThrows(InternalServerErrorException.class, () -> userAuthenticationService.registerInstitute(instituteRegistrationRequest));
//    }

    //Student Registration Tests
    //Teacher Registration Tests with valid information
//    @Test
//    void testRegisterStudent_success_withValidInformation(){
//
//        StudentEntity entity = new StudentEntity();
//        entity.setFirstName("John");
//        entity.setLastName("Doe");
//        entity.setDob(LocalDate.of(2000, 1, 1));
//        entity.setAddress("123 Main St");
//        entity.setContact("1234567890");
//        entity.setEmail("student@test.com");
//        entity.setPassword("Password@123");
//        entity.setRole(Role.ROLE_STUDENT);
//
//        Student model = new Student();
//        model.setFirstName("John");
//        model.setLastName("Doe");
//        model.setDob(LocalDate.of(2000, 1, 1));
//        model.setAddress("123 Main St");
//        model.setContact("1234567890");
//        model.setEmail("student@test.com");
//        model.setPassword("Password@123");
//        model.setRole(Role.ROLE_STUDENT);
//
//        String expectedUserSlug = "john-doe";
//
//        entity.setUserSlug(expectedUserSlug);
//        model.setUserSlug(expectedUserSlug);
//
//        when(mapper.map(studentRegistrationRequest, StudentEntity.class)).thenReturn(entity);
//
//        when(userRepository.save(entity)).thenReturn(entity);
//
//        when(mapper.map(entity, Student.class)).thenReturn(model);
//
//        when(userService.isUserExist("student@test.com")).thenReturn(false);
//
//        when(userService.generateUserSlug("John-Doe")).thenReturn(expectedUserSlug);
//
//        Student result = userAuthenticationService.registerStudent(studentRegistrationRequest);
//
//        assertNotNull(result);
//
//        assertEquals("John", result.getFirstName());
//        assertEquals("Doe", result.getLastName());
//        assertEquals(LocalDate.of(2000, 1, 1), result.getDob());
//        assertEquals("123 Main St", result.getAddress());
//        assertEquals("1234567890", result.getContact());
//        assertEquals("student@test.com", result.getEmail());
//        assertEquals("Password@123", result.getPassword());
//        assertEquals(Role.ROLE_STUDENT, result.getRole());
//        assertEquals(expectedUserSlug, result.getUserSlug());
//
//        verify(userRepository).save(entity);
//        verify(userService).generateUserSlug("John-Doe");
//        verify(encoder).encode("Password@123");
//
//    }

    @Test
    void registerStudent_InvalidPassword_throwsInvalidInputException() {
        // Password does not meet 8-character lengths
        studentRegistrationRequest.setPassword("pass");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Password meets an 8-character length but does not contain at least 1 uppercase character
        studentRegistrationRequest.setPassword("password");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Password meets an 8-character length, at least 1 uppercase character but does not contain at least 1 lower character
        studentRegistrationRequest.setPassword("PASSWORD");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Password meets 8-character length, at least 1 uppercase character and lowercase character but does not contain a special character
        studentRegistrationRequest.setPassword("Password");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Password meets 8-character length contain uppercase character, a special character but does not contain the at least numeric character
        studentRegistrationRequest.setPassword("Password@");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);
    }

    @Test
    void registerStudent_InvalidEmail_throwsInvalidInputException() {
        // Email does not contain '@' symbol
        studentRegistrationRequest.setEmail("invalidemailemail.com");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Email contain '@' symbol but docent contain domain name
        studentRegistrationRequest.setEmail("invalidemail@");
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);
    }

    @Test
    void registerStudent_InvalidDob_throwsInvalidInputException() {
        // Dob contains a future year
        studentRegistrationRequest.setDob(LocalDate.now().plusYears(1));
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Dob contains a future month
        studentRegistrationRequest.setDob(LocalDate.now().plusMonths(1));
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Dob contains a future day
        studentRegistrationRequest.setDob(LocalDate.now().plusDays(1));
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Dob contains current day
        studentRegistrationRequest.setDob(LocalDate.now());
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);

        // Dob contains a date that is less than 6 years old
        studentRegistrationRequest.setDob(LocalDate.now().minusYears(3));
        assertThrows(InvalidInputException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);
    }

    @Test
    void registerStudent_userExists_throwsUserAlreadyExistException() {
        when(userService.isUserExist("student@test.com")).thenReturn(true);
        assertThrows(UserAlreadyExistException.class, () -> userAuthenticationService.registerStudent(studentRegistrationRequest));
        verifyNoInteractions(userRepository, encoder);
    }

    // Teacher Registration Tests
    // Teacher Registration Tests with valid information
//    @Test
//    void testRegisterTeacher_success_withValidInformation(){
//
//        TeacherEntity entity = new TeacherEntity();
//        entity.setFirstName("Jane");
//        entity.setLastName("Doe");
//        entity.setDob(LocalDate.of(1980, 1, 1));
//        entity.setAddress("123 Main St");
//        entity.setContact("1234567890");
//        entity.setEmail("teacher@test.com");
//        entity.setPassword("Password@123");
//        entity.setRole(Role.ROLE_TEACHER);
//
//        Teacher model = new Teacher();
//        model.setFirstName("Jane");
//        model.setLastName("Doe");
//        model.setDob(LocalDate.of(1980, 1, 1));
//        model.setAddress("123 Main St");
//        model.setContact("1234567890");
//        model.setEmail("student@test.com");
//        model.setPassword("Password@123");
//        model.setRole(Role.ROLE_TEACHER);
//
//        String expectedUserSlug = "jane-doe";
//
//        entity.setUserSlug(expectedUserSlug);
//        model.setUserSlug(expectedUserSlug);
//
//        when(mapper.map(teacherRegistrationRequest, TeacherEntity.class)).thenReturn(entity);
//
//        when(userRepository.save(entity)).thenReturn(entity);
//
//        when(mapper.map(entity, Teacher.class)).thenReturn(model);
//
//        when(userService.isUserExist("teacher@test.com")).thenReturn(false);
//
//        when(userService.generateUserSlug("Jane-Doe")).thenReturn(expectedUserSlug);
//
//        Teacher result = userAuthenticationService.registerTeacher(teacherRegistrationRequest);
//
//        assertNotNull(result);
//
//        assertEquals("Jane", result.getFirstName());
//        assertEquals("Doe", result.getLastName());
//        assertEquals(LocalDate.of(1980, 1, 1), result.getDob());
//        assertEquals("123 Main St", result.getAddress());
//        assertEquals("1234567890", result.getContact());
//        assertEquals("student@test.com", result.getEmail());
//        assertEquals("Password@123", result.getPassword());
//        assertEquals(Role.ROLE_TEACHER, result.getRole());
//        assertEquals(expectedUserSlug, result.getUserSlug());
//
//        verify(userRepository).save(entity);
//        verify(userService).generateUserSlug("Jane-Doe");
//        verify(encoder).encode("Password@123");
//
//    }

    // Verify User Login Tests
    @Test
    void verify_success_genericUser() {
        UserEntity entity = new UserEntity();
        entity.setEmail("user@test.com");
        User model = new User();

        Authentication auth = mock(Authentication.class);

        when(auth.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtService.generateToken(auth)).thenReturn("jwt-token");
        when(userRepository.findByEmail("user@test.com")).thenReturn(entity);
        when(mapper.map(entity, User.class)).thenReturn(model);

        AuthResponse response = userAuthenticationService.verify(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(model, response.getUser());
    }

//    @Test
//    void verify_success_InstituteUser() {
//        loginRequest.setEmail("institute@test.com");
//
//        InstituteEntity entity = new InstituteEntity();
//        entity.setEmail("institute@test.com");
//        Institute model = new Institute();
//        model.setEmail("institute@test.com");
//
//        Authentication auth = mock(Authentication.class);
//
//        when(auth.isAuthenticated()).thenReturn(true);
//        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
//        when(jwtService.generateToken(auth)).thenReturn("jwt-token");
//        when(userRepository.findByEmail("institute@test.com")).thenReturn(entity);
//        when(mapper.map(entity, Institute.class)).thenReturn(model);
//
//        AuthResponse response = userAuthenticationService.verify(loginRequest);
//
//        assertNotNull(response);
//        assertEquals("jwt-token", response.getToken());
//        assertEquals(model, response.getUser());
//    }

//    @Test
//    void verify_success_TeacherUser() {
//        loginRequest.setEmail("teacher@test.com");
//
//        TeacherEntity entity = new TeacherEntity();
//        entity.setEmail("teacher@test.com");
//        Teacher model = new Teacher();
//        model.setEmail("teacher@test.com");
//
//        Authentication auth = mock(Authentication.class);
//
//        when(auth.isAuthenticated()).thenReturn(true);
//        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
//        when(jwtService.generateToken(auth)).thenReturn("jwt-token");
//        when(userRepository.findByEmail("teacher@test.com")).thenReturn(entity);
//        when(mapper.map(entity, Teacher.class)).thenReturn(model);
//
//        AuthResponse response = userAuthenticationService.verify(loginRequest);
//
//        assertNotNull(response);
//        assertEquals("jwt-token", response.getToken());
//        assertEquals(model, response.getUser());
//    }

//    @Test
//    void verify_success_StudentUser() {
//        loginRequest.setEmail("student@test.com");
//
//        StudentEntity entity = new StudentEntity();
//        entity.setEmail("student@test.com");
//        Student model = new Student();
//        model.setEmail("student@test.com");
//
//        Authentication auth = mock(Authentication.class);
//
//        when(auth.isAuthenticated()).thenReturn(true);
//        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
//        when(jwtService.generateToken(auth)).thenReturn("jwt-token");
//        when(userRepository.findByEmail("student@test.com")).thenReturn(entity);
//        when(mapper.map(entity, Student.class)).thenReturn(model);
//
//        AuthResponse response = userAuthenticationService.verify(loginRequest);
//
//        assertNotNull(response);
//        assertEquals("jwt-token", response.getToken());
//        assertEquals(model, response.getUser());
//    }

    @Test
    void verify_failed_invalidEmail() {
        // Email does not contain '@' symbol
        loginRequest.setEmail("invalidemailemail.com");
        assertThrows(RuntimeException.class, () -> userAuthenticationService.verify(loginRequest));
        verifyNoInteractions(userRepository, encoder);

        // Email contain '@' symbol but docent contain domain name
        loginRequest.setEmail("invalidemail@");
        assertThrows(RuntimeException.class, () -> userAuthenticationService.verify(loginRequest));
        verifyNoInteractions(userRepository, encoder);
    }

    @Test
    void verify_authenticationFailure_throwsUnauthorizedException() {
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});
        assertThrows(UnauthorizedException.class, () -> userAuthenticationService.verify(loginRequest));
        verifyNoInteractions(jwtService, userRepository);
    }

    @Test
    void verify_nullPointerException_throwsInternalServerErrorException() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(null);
        assertThrows(InternalServerErrorException.class, () -> userAuthenticationService.verify(loginRequest));
    }

    @Test
    void verify_unauthenticated_throwsUnauthorizedException() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        assertThrows(InternalServerErrorException.class, () -> userAuthenticationService.verify(loginRequest));
        verifyNoInteractions(jwtService, userRepository);
    }
}
