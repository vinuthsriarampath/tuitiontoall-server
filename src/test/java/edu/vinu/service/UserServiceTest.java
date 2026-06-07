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

package edu.vinu.service;

import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.entity.user_entities.StudentEntity;
import edu.vinu.entity.user_entities.TeacherEntity;
import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.common.exception.custom.InternalServerErrorException;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.model.user_models.Institute;
import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.request.update_user_details.InstituteDetailsUpdateRequest;
import edu.vinu.request.update_user_details.StudentDetailsUpdateRequest;
import edu.vinu.request.update_user_details.TeacherDetailsUpdateRequest;
import edu.vinu.service.common.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.DisabledException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;
    // Test getUserByEmail success case
    @Test
    void testGetUserByEmail_shouldReturnUser_whenUserExists() {
        String email = "test@example.com";
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setEmail(email);
        User expectedUser = new User();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(mockUserEntity);
        when(mapper.map(mockUserEntity, User.class)).thenReturn(expectedUser);

        User actualUser = userService.getUserByEmail(email);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        verify(userRepository).findByEmail(email);
    }

    // Test getUserByEmail when the user does not exist
    @Test
    void testGetUserByEmail_shouldThrowException_whenUserDoesNotExist() {
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrowsExactly(NotFoundException.class, () -> userService.getUserByEmail(email),
                "A user from " + email + " not found!!");
        verify(userRepository).findByEmail(email);
    }

    // Test isUserExist when user exists
    @Test
    void testIsUserExistsByEmail_shouldReturnTrue_whenUserExists() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertTrue(userService.isUserExist(email));
        verify(userRepository).existsByEmail(email);
    }

    // Test isUserExist when a user does not exist
    @Test
    void testIsUserExistsByEmail_shouldReturnFalse_whenUserDoesNotExist() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertFalse(userService.isUserExist(email));
        verify(userRepository).existsByEmail(email);
    }

    // Test isUserDisabled when a user is disabled
    @Test
    void testIsUserDisabled_shouldReturnTrue_whenUserIsDisabled() {
        String email = "test@example.com";
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setEmail(email);
        when(userRepository.isUserDisabledByEmail(email)).thenReturn(true);

        assertTrue(userService.isUserDisabled(mockUserEntity));
        verify(userRepository).isUserDisabledByEmail(email);
    }

    // Test getAllUsersByFirstNameLike success case
    @Test
    void testGetAllUsersByFirstNameLike_shouldReturnListOfTeachersAndStudents_whenUserFirstNameIsLike() {
//        String firstName = "John";
//        List<StudentEntity> mockStudentEntities = List.of(new StudentEntity(), new StudentEntity());
//        List<TeacherEntity> mockTeacherEntities = List.of(new TeacherEntity(), new TeacherEntity());
//        List<Student> expectedStudents = List.of(new Student(), new Student());
//        List<Teacher> expectedTeachers = List.of(new Teacher(), new Teacher());
//
//        when(userRepository.getStudentsByFirstNameLike(firstName)).thenReturn(mockStudentEntities);
//        when(userRepository.getTeachersByFirstNameLike(firstName)).thenReturn(mockTeacherEntities);
//        when(mapper.map(mockStudentEntities.get(0), Student.class)).thenReturn(expectedStudents.get(0));
//        when(mapper.map(mockStudentEntities.get(1), Student.class)).thenReturn(expectedStudents.get(1));
//        when(mapper.map(mockTeacherEntities.get(0), Teacher.class)).thenReturn(expectedTeachers.get(0));
//        when(mapper.map(mockTeacherEntities.get(1), Teacher.class)).thenReturn(expectedTeachers.get(1));
//
//        List<User> actualUsers = userService.getAllUsersByFirstNameLike(firstName);
//
//        assertNotNull(actualUsers);
//        assertEquals(4, actualUsers.size());
//        verify(userRepository).getTeachersByFirstNameLike(firstName);
//        verify(userRepository).getStudentsByFirstNameLike(firstName);
    }

    // Test getAllUsersByFirstNameLike when no users found
    @Test
    void testGetAllUsersByFirstNameLike_shouldThrowUserNotFoundException_whenEmptyListOfUsers() {
//        String firstName = "John";
//        when(userRepository.getStudentsByFirstNameLike(firstName)).thenReturn(List.of());
//        when(userRepository.getTeachersByFirstNameLike(firstName)).thenReturn(List.of());
//
//        assertThrowsExactly(NotFoundException.class, () ->
//                        userService.getAllUsersByFirstNameLike(firstName),
//                "There are no users starts with " + firstName);
//        verify(userRepository).getTeachersByFirstNameLike(firstName);
//        verify(userRepository).getStudentsByFirstNameLike(firstName);
    }

    // Test getAllStudentsByFirstNameLike success case
    @Test
    void testGetAllStudentsByFirstNameLike_shouldReturnListOfStudents_whenUserFirstNameIsLike() {
//        String firstName = "John";
//        List<StudentEntity> mockStudentEntities = List.of(new StudentEntity(), new StudentEntity());
//        List<Student> expectedStudents = List.of(new Student(), new Student());
//
//        when(userRepository.getStudentsByFirstNameLike(firstName)).thenReturn(mockStudentEntities);
//        when(mapper.map(mockStudentEntities.get(0), Student.class)).thenReturn(expectedStudents.get(0));
//        when(mapper.map(mockStudentEntities.get(1), Student.class)).thenReturn(expectedStudents.get(1));
//
//        List<Student> actualStudents = userService.getAllStudentsByFirstNameLike(firstName);
//
//        assertNotNull(actualStudents);
//        assertEquals(expectedStudents.size(), actualStudents.size());
//        verify(userRepository).getStudentsByFirstNameLike(firstName);
    }

    // Test getAllStudentsByFirstNameLike when no students found
    @Test
    void testGetAllStudentsByFirstNameLike_shouldReturnEmptyListOfStudents_whenUserFirstNameIsLike() {
//        String firstName = "John";
//        when(userRepository.getStudentsByFirstNameLike(firstName)).thenReturn(List.of());
//
//        List<Student> actualStudents = userService.getAllStudentsByFirstNameLike(firstName);
//
//        assertNotNull(actualStudents);
//        assertEquals(0, actualStudents.size());
//        verify(userRepository).getStudentsByFirstNameLike(firstName);
    }

    // Test getAllTeachersByFirstNameLike success case
    @Test
    void testGetAllTeachersByFirstNameLike_shouldReturnListOfTeachers_whenUserFirstNameIsLike() {
//        String firstName = "John";
//        List<TeacherEntity> mockTeacherEntities = List.of(new TeacherEntity(), new TeacherEntity());
//        List<Teacher> expectedTeachers = List.of(new Teacher(), new Teacher());
//
//        when(userRepository.getTeachersByFirstNameLike(firstName)).thenReturn(mockTeacherEntities);
//        when(mapper.map(mockTeacherEntities.get(0), Teacher.class)).thenReturn(expectedTeachers.get(0));
//        when(mapper.map(mockTeacherEntities.get(1), Teacher.class)).thenReturn(expectedTeachers.get(1));
//
//        List<Teacher> actualTeachers = userService.getAllTeachersByFirstNameLike(firstName);
//
//        assertNotNull(actualTeachers);
//        assertEquals(expectedTeachers.size(), actualTeachers.size());
//        verify(userRepository).getTeachersByFirstNameLike(firstName);
    }

    // Test getAllTeachersByFirstNameLike when no teachers found
    @Test
    void testGetAllTeachersByFirstNameLike_shouldReturnEmptyListOfTeachers_whenUserFirstNameIsLike() {
//        String firstName = "John";
//        when(userRepository.getTeachersByFirstNameLike(firstName)).thenReturn(List.of());
//
//        List<Teacher> actualTeachers = userService.getAllTeachersByFirstNameLike(firstName);
//
//        assertNotNull(actualTeachers);
//        assertEquals(0, actualTeachers.size());
//        verify(userRepository).getTeachersByFirstNameLike(firstName);
    }

    // Test getAllStudents a success case
    @Test
    void testGetAllStudents_shouldReturnListOfAllStudents() {
        List<StudentEntity> mockStudentEntities = List.of(new StudentEntity(), new StudentEntity());
        List<Student> expectedStudents = List.of(new Student(), new Student());

//        when(userRepository.getAllStudents()).thenReturn(mockStudentEntities);
//        when(mapper.map(mockStudentEntities.get(0), Student.class)).thenReturn(expectedStudents.get(0));
//        when(mapper.map(mockStudentEntities.get(1), Student.class)).thenReturn(expectedStudents.get(1));
//
//        List<Student> actualStudents = userService.getAllStudents();
//
//        assertNotNull(actualStudents);
//        assertEquals(expectedStudents.size(), actualStudents.size());
//        verify(userRepository).getAllStudents();
    }

    // Test getAllStudents when no students found
    @Test
    void testGetAllStudents_shouldThrowUserNotFoundException_whenNoStudentsFound() {
//        when(userRepository.getAllStudents()).thenReturn(List.of());
//
//        assertThrowsExactly(NotFoundException.class, () -> userService.getAllStudents(),
//                "No Students Found");
//        verify(userRepository).getAllStudents();
    }

    // Test getAllTeachers success case
    @Test
    void testGetAllTeachers_shouldReturnListOfAllTeachers() {
//        List<TeacherEntity> mockTeacherEntities = List.of(new TeacherEntity(), new TeacherEntity());
//        List<Teacher> expectedTeachers = List.of(new Teacher(), new Teacher());
//
//        when(userRepository.getAllTeachers()).thenReturn(mockTeacherEntities);
//        when(mapper.map(mockTeacherEntities.get(0), Teacher.class)).thenReturn(expectedTeachers.get(0));
//        when(mapper.map(mockTeacherEntities.get(1), Teacher.class)).thenReturn(expectedTeachers.get(1));
//
//        List<Teacher> actualTeachers = userService.getAllTeachers();
//
//        assertNotNull(actualTeachers);
//        assertEquals(expectedTeachers.size(), actualTeachers.size());
//        verify(userRepository).getAllTeachers();
    }

    // Test getAllTeachers when no teachers found
    @Test
    void testGetAllTeachers_shouldThrowUserNotFoundException_whenNoTeachersFound() {
//        when(userRepository.getAllTeachers()).thenReturn(List.of());
//
//        assertThrowsExactly(NotFoundException.class, () -> userService.getAllTeachers(),
//                "No Teachers Found!");
//        verify(userRepository).getAllTeachers();
    }

    // Test getAllInstitutes a success case
    @Test
    void testGetAllInstitutes_shouldReturnListOfAllInstitutes() {
//        List<InstituteEntity> mockInstituteEntities = List.of(new InstituteEntity(), new InstituteEntity());
//        List<Institute> expectedInstitutes = List.of(new Institute(), new Institute());
//
//        when(userRepository.getAllInstitutes()).thenReturn(mockInstituteEntities);
//        when(mapper.map(mockInstituteEntities.get(0), Institute.class)).thenReturn(expectedInstitutes.get(0));
//        when(mapper.map(mockInstituteEntities.get(1), Institute.class)).thenReturn(expectedInstitutes.get(1));
//
//        List<Institute> actualInstitutes = userService.getAllInstitutes();
//
//        assertNotNull(actualInstitutes);
//        assertEquals(expectedInstitutes.size(), actualInstitutes.size());
    }

    // Test getAllInstitutes when no institutes found
    @Test
    void testGetAllInstitutes_shouldThrowUserNotFoundException_whenNoInstitutesFound() {
//        when(userRepository.getAllInstitutes()).thenReturn(List.of());
//
//        assertThrowsExactly(NotFoundException.class, () -> userService.getAllInstitutes(),
//                "No Institutes Found!");
//        verify(userRepository).getAllInstitutes();
    }

    // Test getAllInstitutesByInstituteName success case
    @Test
    void testGetAllInstitutesByInstituteName_shouldReturnListOfInstitutes_whenInstituteNameIsLike() {
//        String instituteName = "ABC Institute";
//        List<InstituteEntity> mockInstituteEntities = List.of(new InstituteEntity(), new InstituteEntity());
//        List<Institute> expectedInstitutes = List.of(new Institute(), new Institute());
//
//        when(userRepository.findByInstituteName(instituteName)).thenReturn(mockInstituteEntities);
//        when(mapper.map(mockInstituteEntities.get(0), Institute.class)).thenReturn(expectedInstitutes.get(0));
//        when(mapper.map(mockInstituteEntities.get(1), Institute.class)).thenReturn(expectedInstitutes.get(1));
//
//        List<Institute> actualInstitutes = userService.getAllInstitutesByInstituteName(instituteName);
//
//        assertNotNull(actualInstitutes);
//        assertEquals(expectedInstitutes.size(), actualInstitutes.size());
//        verify(userRepository).findByInstituteName(instituteName);
    }

    // Test getAllInstitutesByInstituteName when no institutes found
    @Test
    void testGetAllInstitutesByInstituteName_shouldReturnEmptyListOfInstitutes_whenInstituteNameIsNotLike() {
//        String instituteName = "ABC Institute";
//        when(userRepository.findByInstituteName(instituteName)).thenReturn(List.of());
//
//        List<Institute> actualInstitutes = userService.getAllInstitutesByInstituteName(instituteName);
//
//        assertNotNull(actualInstitutes);
//        assertEquals(0, actualInstitutes.size());
//        verify(userRepository).findByInstituteName(instituteName);
    }

    // Test updateInstituteDetails success case
    @Test
    void testUpdateInstituteDetails_shouldReturnUpdatedInstitute_whenInstituteDetailsAreUpdatedSuccessfully() {
//        String email = "institute@example.com";
//        InstituteDetailsUpdateRequest request = new InstituteDetailsUpdateRequest();
//        request.setInstituteName("New Institute");
//        request.setAddress("123 New Street");
//        request.setContact("9876543210");
//
//        InstituteEntity mockInstituteEntity = new InstituteEntity();
//        mockInstituteEntity.setEmail(email);
//        InstituteEntity updatedInstituteEntity = new InstituteEntity();
//        updatedInstituteEntity.setEmail(email);
//        updatedInstituteEntity.setInstituteName("New Institute");
//        updatedInstituteEntity.setAddress("123 New Street");
//        updatedInstituteEntity.setContact("9876543210");
//
//        Institute expectedInstitute = new Institute();
//        expectedInstitute.setInstituteName("New Institute");
//        expectedInstitute.setAddress("123 New Street");
//        expectedInstitute.setContact("9876543210");
//
//        when(userRepository.existsByEmail(email)).thenReturn(true);
//        when(userRepository.findByEmail(email)).thenReturn(mockInstituteEntity);
//        when(userRepository.save(any(InstituteEntity.class))).thenReturn(updatedInstituteEntity);
//        when(mapper.map(updatedInstituteEntity, Institute.class)).thenReturn(expectedInstitute);
//
//        Institute actualInstitute = userService.updateInstituteDetails(email, request);
//
//        assertNotNull(actualInstitute);
//        assertEquals(expectedInstitute.getInstituteName(), actualInstitute.getInstituteName());
//        assertEquals(expectedInstitute.getAddress(), actualInstitute.getAddress());
//        assertEquals(expectedInstitute.getContact(), actualInstitute.getContact());
//        verify(userRepository).existsByEmail(email);
//        verify(userRepository).findByEmail(email);
//        verify(userRepository).save(any(InstituteEntity.class));
    }

    // Test updateInstituteDetails when user not found
    @Test
    void testUpdateInstituteDetails_shouldThrowUserNotFoundException_whenUserNotFound() {
        String email = "nonexistent@example.com";
        InstituteDetailsUpdateRequest request = new InstituteDetailsUpdateRequest();

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertThrowsExactly(NotFoundException.class, () ->
                        userService.updateInstituteDetails(email, request),
                "User not found for " + email);
        verify(userRepository).existsByEmail(email);
    }

    // Test updateInstituteDetails when the user is not an institute
    @Test
    void testUpdateInstituteDetails_shouldThrowUserNotFoundException_whenUserIsNotInstitute() {
        String email = "user@example.com";
        InstituteDetailsUpdateRequest request = new InstituteDetailsUpdateRequest();
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(userEntity);

        assertThrowsExactly(NotFoundException.class, () ->
                        userService.updateInstituteDetails(email, request),
                "Institute not found for " + email);
        verify(userRepository).existsByEmail(email);
        verify(userRepository).findByEmail(email);
    }

    // Test updateTeacherDetails success case
    @Test
    void testUpdateTeacherDetails_shouldReturnUpdatedTeacher_whenDetailsAreValid() {
//        String email = "teacher@example.com";
//        TeacherDetailsUpdateRequest request = new TeacherDetailsUpdateRequest();
//        request.setFirstName("John");
//        request.setLastName("Doe");
//        request.setAddress("456 Teacher Lane");
//        request.setContact("1234567890");
//        request.setDob(LocalDate.of(1980, 1, 1));
//
//        TeacherEntity mockTeacherEntity = new TeacherEntity();
//        mockTeacherEntity.setEmail(email);
//        TeacherEntity updatedTeacherEntity = new TeacherEntity();
//        updatedTeacherEntity.setEmail(email);
//        updatedTeacherEntity.setFirstName("John");
//        updatedTeacherEntity.setLastName("Doe");
//        updatedTeacherEntity.setAddress("456 Teacher Lane");
//        updatedTeacherEntity.setContact("1234567890");
//        updatedTeacherEntity.setDob(LocalDate.of(1980, 1, 1));
//
//        Teacher expectedTeacher = new Teacher();
//        expectedTeacher.setFirstName("John");
//        expectedTeacher.setLastName("Doe");
//        expectedTeacher.setAddress("456 Teacher Lane");
//        expectedTeacher.setContact("1234567890");
//
//        when(userRepository.existsByEmail(email)).thenReturn(true);
//        when(userRepository.findByEmail(email)).thenReturn(mockTeacherEntity);
//        when(userRepository.save(any(TeacherEntity.class))).thenReturn(updatedTeacherEntity);
//        when(mapper.map(updatedTeacherEntity, Teacher.class)).thenReturn(expectedTeacher);
//
//        Teacher actualTeacher = userService.updateTeacherDetails(email, request);
//
//        assertNotNull(actualTeacher);
//        assertEquals(expectedTeacher.getFirstName(), actualTeacher.getFirstName());
//        assertEquals(expectedTeacher.getLastName(), actualTeacher.getLastName());
//        verify(userRepository).existsByEmail(email);
//        verify(userRepository).findByEmail(email);
//        verify(userRepository).save(any(TeacherEntity.class));
    }

    // Test updateTeacherDetails when user not found
    @Test
    void testUpdateTeacherDetails_shouldThrowUserNotFoundException_whenUserNotFound() {
        String email = "nonexistent@example.com";
        TeacherDetailsUpdateRequest request = new TeacherDetailsUpdateRequest();

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertThrowsExactly(NotFoundException.class, () ->
                        userService.updateTeacherDetails(email, request),
                "No User Found By " + email);
        verify(userRepository).existsByEmail(email);
    }

    // Test updateTeacherDetails when DOB is invalid
    @Test
    void testUpdateTeacherDetails_shouldThrowInvalidInputException_whenDobIsInvalid() {
        String email = "teacher@example.com";
        TeacherDetailsUpdateRequest request = new TeacherDetailsUpdateRequest();
        request.setDob(LocalDate.now()); // Invalid DOB (too young)

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrowsExactly(InvalidInputException.class, () ->
                        userService.updateTeacherDetails(email, request),
                "You must be at least 6 years old");
        verify(userRepository).existsByEmail(email);
    }

    // Test updateTeacherDetails when the user is not a teacher
    @Test
    void testUpdateTeacherDetails_shouldThrowUserNotFoundException_whenUserIsNotTeacher() {
        String email = "user@example.com";
        TeacherDetailsUpdateRequest request = new TeacherDetailsUpdateRequest();
        request.setDob(LocalDate.of(1980, 1, 1));
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(userEntity);

        assertThrowsExactly(NotFoundException.class, () ->
                        userService.updateTeacherDetails(email, request),
                "No teacher found by " + email);
        verify(userRepository).existsByEmail(email);
        verify(userRepository).findByEmail(email);
    }

    // Test updateStudentDetails success case
    @Test
    void testUpdateStudentDetails_shouldReturnUpdatedStudent_whenDetailsAreValid() {
//        String email = "student@example.com";
//        StudentDetailsUpdateRequest request = new StudentDetailsUpdateRequest();
//        request.setFirstName("Jane");
//        request.setLastName("Doe");
//        request.setAddress("789 Student Road");
//        request.setContact("0987654321");
//        request.setDob(LocalDate.of(2000, 1, 1));
//
//        StudentEntity mockStudentEntity = new StudentEntity();
//        mockStudentEntity.setEmail(email);
//        StudentEntity updatedStudentEntity = new StudentEntity();
//        updatedStudentEntity.setEmail(email);
//        updatedStudentEntity.setFirstName("Jane");
//        updatedStudentEntity.setLastName("Doe");
//        updatedStudentEntity.setAddress("789 Student Road");
//        updatedStudentEntity.setContact("0987654321");
//        updatedStudentEntity.setDob(LocalDate.of(2000, 1, 1));
//
//        Student expectedStudent = new Student();
//        expectedStudent.setFirstName("Jane");
//        expectedStudent.setLastName("Doe");
//        expectedStudent.setAddress("789 Student Road");
//        expectedStudent.setContact("0987654321");
//
//        when(userRepository.existsByEmail(email)).thenReturn(true);
//        when(userRepository.findByEmail(email)).thenReturn(mockStudentEntity);
//        when(userRepository.save(any(StudentEntity.class))).thenReturn(updatedStudentEntity);
//        when(mapper.map(updatedStudentEntity, Student.class)).thenReturn(expectedStudent);
//
//        Student actualStudent = userService.updateStudentDetails(email, request);
//
//        assertNotNull(actualStudent);
//        assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
//        assertEquals(expectedStudent.getLastName(), actualStudent.getLastName());
//        verify(userRepository).existsByEmail(email);
//        verify(userRepository).findByEmail(email);
//        verify(userRepository).save(any(StudentEntity.class));
    }

    // Test updateStudentDetails when user not found
    @Test
    void testUpdateStudentDetails_shouldThrowUserNotFoundException_whenUserNotFound() {
        String email = "nonexistent@example.com";
        StudentDetailsUpdateRequest request = new StudentDetailsUpdateRequest();

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertThrowsExactly(NotFoundException.class, () ->
                        userService.updateStudentDetails(email, request),
                "No User Found By " + email);
        verify(userRepository).existsByEmail(email);
    }

    // Test updateStudentDetails when DOB is invalid
    @Test
    void testUpdateStudentDetails_shouldThrowInvalidInputException_whenDobIsInvalid() {
        String email = "student@example.com";
        StudentDetailsUpdateRequest request = new StudentDetailsUpdateRequest();
        request.setDob(LocalDate.now()); // Invalid DOB (too young)

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrowsExactly(InvalidInputException.class, () ->
                        userService.updateStudentDetails(email, request),
                "You must be at least 6 years old");
        verify(userRepository).existsByEmail(email);
    }

    // Test updateStudentDetails when the user is not a student
    @Test
    void testUpdateStudentDetails_shouldThrowUserNotFoundException_whenUserIsNotStudent() {
        String email = "user@example.com";
        StudentDetailsUpdateRequest request = new StudentDetailsUpdateRequest();
        request.setDob(LocalDate.of(2000, 1, 1));
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(userEntity);

        assertThrowsExactly(NotFoundException.class, () ->
                        userService.updateStudentDetails(email, request),
                "No Student found by " + email);
        verify(userRepository).existsByEmail(email);
        verify(userRepository).findByEmail(email);
    }

    // Test disableUserAccountByEmail a success case
    @Test
    void testDisableUserAccountByEmail_shouldDisableUser_whenUserExistsAndNotDisabled() {
        String email = "user@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(userEntity);
        when(userRepository.isUserDisabledByEmail(email)).thenReturn(false);

        userService.disableUserAccountByEmail(email);

        verify(userRepository).findByEmail(email);
        verify(userRepository).isUserDisabledByEmail(email);
        verify(userRepository).delete(userEntity);
    }

    // Test disableUserAccountByEmail when user not found
    @Test
    void testDisableUserAccountByEmail_shouldThrowUserNotFoundException_whenUserNotFound() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrowsExactly(NotFoundException.class, () ->
                        userService.disableUserAccountByEmail(email),
                "User not found by " + email);
        verify(userRepository).findByEmail(email);
    }

    // Test disableUserAccountByEmail when the user is already disabled
    @Test
    void testDisableUserAccountByEmail_shouldThrowDisabledException_whenUserIsAlreadyDisabled() {
        String email = "user@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(userEntity);
        when(userRepository.isUserDisabledByEmail(email)).thenReturn(true);

        assertThrowsExactly(DisabledException.class, () ->
                        userService.disableUserAccountByEmail(email),
                "User is disabled Already");
        verify(userRepository).findByEmail(email);
        verify(userRepository).isUserDisabledByEmail(email);
    }

    // Test getUserByUserSlug success case
    @Test
    void testGetUserByUserSlug_shouldReturnUser_whenUserExistsAndNotDisabled() {
        String userSlug = "john-doe";
        String email = "john.doe@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setUserSlug(userSlug);
        userEntity.setEmail(email);
        User expectedUser = new User();
        expectedUser.setUserSlug(userSlug);

        when(userRepository.findByUserSlug(userSlug)).thenReturn(userEntity);
        when(userRepository.isUserDisabledByEmail(email)).thenReturn(false);
        when(mapper.map(userEntity, User.class)).thenReturn(expectedUser);

        User actualUser = userService.getUserByUserSlug(userSlug);

        assertNotNull(actualUser);
        assertEquals(expectedUser.getUserSlug(), actualUser.getUserSlug());
        verify(userRepository).findByUserSlug(userSlug);
        verify(userRepository).isUserDisabledByEmail(email);
    }

    // Test getUserByUserSlug when user not found
    @Test
    void testGetUserByUserSlug_shouldThrowUserNotFoundException_whenUserNotFound() {
        String userSlug = "nonexistent";
        when(userRepository.findByUserSlug(userSlug)).thenReturn(null);

        assertThrowsExactly(NotFoundException.class, () ->
                        userService.getUserByUserSlug(userSlug),
                "No user found by " + userSlug);
        verify(userRepository).findByUserSlug(userSlug);
    }

    // Test getUserByUserSlug when the user is disabled
    @Test
    void testGetUserByUserSlug_shouldThrowDisabledException_whenUserIsDisabled() {
        String userSlug = "john-doe";
        String email = "john.doe@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setUserSlug(userSlug);
        userEntity.setEmail(email);

        when(userRepository.findByUserSlug(userSlug)).thenReturn(userEntity);
        when(userRepository.isUserDisabledByEmail(email)).thenReturn(true);

        assertThrowsExactly(DisabledException.class, () ->
                        userService.getUserByUserSlug(userSlug),
                "User is disabled");
        verify(userRepository).findByUserSlug(userSlug);
        verify(userRepository).isUserDisabledByEmail(email);
    }

    // Test generateUserSlug when slug is unique
    @Test
    void testGenerateUserSlug_shouldReturnUniqueSlug_whenNoDuplicates() {
        String base = "John Doe";
        String expectedSlug = "john-doe";

        when(userRepository.existsByUserSlug(expectedSlug)).thenReturn(false);

        String actualSlug = userService.generateUserSlug(base);

        assertEquals(expectedSlug, actualSlug);
        verify(userRepository).existsByUserSlug(expectedSlug);
    }

    // Test generateUserSlug when slug needs a counter
    @Test
    void testGenerateUserSlug_shouldAppendCounter_whenSlugExists() {
        String base = "John Doe";
        String baseSlug = "john-doe";
        String expectedSlug = "john-doe-1";

        when(userRepository.existsByUserSlug(baseSlug)).thenReturn(true);
        when(userRepository.existsByUserSlug(expectedSlug)).thenReturn(false);

        String actualSlug = userService.generateUserSlug(base);

        assertEquals(expectedSlug, actualSlug);
        verify(userRepository).existsByUserSlug(baseSlug);
        verify(userRepository).existsByUserSlug(expectedSlug);
    }

    // Test convertToModel with StudentEntity
    @Test
    void testConvertToModel_withStudentEntity() {
//        StudentEntity studentEntity = new StudentEntity();
//        Student student = new Student();
//        when(mapper.map(studentEntity, Student.class)).thenReturn(student);
//
//        User result = userService.convertToModel(studentEntity);
//        assertEquals(student, result);
    }

    // Test convertToModel with TeacherEntity
    @Test
    void testConvertToModel_withTeacherEntity() {
//        TeacherEntity teacherEntity = new TeacherEntity();
//        Teacher teacher = new Teacher();
//        when(mapper.map(teacherEntity, Teacher.class)).thenReturn(teacher);
//
//        User result = userService.convertToModel(teacherEntity);
//        assertEquals(teacher, result);
    }

    // Test convertToModel with InstituteEntity
    @Test
    void testConvertToModel_withInstituteEntity() {
//        InstituteEntity instituteEntity = new InstituteEntity();
//        Institute institute = new Institute();
//        when(mapper.map(instituteEntity, Institute.class)).thenReturn(institute);
//
//        User result = userService.convertToModel(instituteEntity);
//        assertEquals(institute, result);
    }

    // Test convertToModel with generic UserEntity
    @Test
    void testConvertToModel_withGenericUserEntity() {
        UserEntity userEntity = new UserEntity();
        User user = new User();
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        User result = userService.convertToModel(userEntity);
        assertEquals(user, result);
    }

    // Test convertToModel when ClassCastException is thrown
    @Test
    void testConvertToModel_whenClassCastExceptionThrown() {
        UserEntity userEntity = new UserEntity();
        when(mapper.map(userEntity, User.class)).thenThrow(new ClassCastException("Mapping error"));

        InternalServerErrorException ex = assertThrows(InternalServerErrorException.class, () ->
                userService.convertToModel(userEntity));
        assertEquals("Mapping error", ex.getMessage());
    }

    // Test convertToInstituteModel when InstituteEntity is pass
    @Test
    void testConvertToInstituteModel_shouldReturnInstituteModel_whenInstituteEntityIsPassed(){
        InstituteEntity instituteEntity = new InstituteEntity();
        Institute expectedInstitute = new Institute();
        when(mapper.map(instituteEntity, Institute.class)).thenReturn(expectedInstitute);

        Institute actualUser = userService.convertToInstituteModel(instituteEntity);

        assertNotNull(actualUser);
        assertEquals(expectedInstitute, actualUser);
    }

    // Test convertToInstituteModel throws InternalServerErrorException
    @Test
    void testConvertToInstituteModel_shouldThrowInternalServerErrorException() {
        InstituteEntity instituteEntity = new InstituteEntity();
        when(mapper.map(instituteEntity, Institute.class)).thenThrow(new ClassCastException("Mapping error"));

        assertThrowsExactly(InternalServerErrorException.class, () ->
                userService.convertToInstituteModel(instituteEntity));
    }

    // Test convertToTeacherModel when TeacherEntity is pass
    @Test
    void testConvertToTeacherModel_shouldReturnTeacherModel_whenTeacherEntityIsPassed(){
        TeacherEntity teacherEntity = new TeacherEntity();
        Teacher expectedTeacher = new Teacher();
        when(mapper.map(teacherEntity, Teacher.class)).thenReturn(expectedTeacher);

        Teacher actualTeacher = userService.convertToTeacherModel(teacherEntity);

        assertNotNull(actualTeacher);
        assertEquals(expectedTeacher, actualTeacher);
    }

    // Test convertToTeacherModel throws InternalServerErrorException
    @Test
    void testConvertToTeacherModel_shouldThrowInternalServerErrorException_whenAnotherTypeEntityIsPassed() {
        TeacherEntity teacherEntity = new TeacherEntity();
        when(mapper.map(teacherEntity, Teacher.class)).thenThrow(new ClassCastException("Mapping error"));

        assertThrowsExactly(InternalServerErrorException.class, () ->
                userService.convertToTeacherModel(teacherEntity));
    }

    // Test convertToStudentModel when StudentEntity is pass
    @Test
    void testConvertToStudentModel_shouldReturnStudentModel_whenStudentEntityIsPassed(){
        StudentEntity studentEntity = new StudentEntity();
        Student expectedStudent = new Student();
        when(mapper.map(studentEntity, Student.class)).thenReturn(expectedStudent);

        Student actualStudent = userService.convertToStudentModel(studentEntity);

        assertNotNull(actualStudent);
        assertEquals(expectedStudent, actualStudent);
    }

    // Test convertToStudentModel throws InternalServerErrorException
    @Test
    void testConvertToStudentModel_shouldThrowInternalServerErrorException_whenAnotherTypeEntityIsPassed() {
        StudentEntity studentEntity = new StudentEntity();
        when(mapper.map(studentEntity, Student.class)).thenThrow(new ClassCastException("Mapping error"));

        assertThrowsExactly(InternalServerErrorException.class, () ->
                userService.convertToStudentModel(studentEntity));
    }
}
