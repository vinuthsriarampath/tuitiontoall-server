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
import edu.vinu.exception.custom.InternalServerErrorException;
import edu.vinu.exception.custom.UserNotFoundException;
import edu.vinu.model.user_models.Institute;
import edu.vinu.model.user_models.Student;
import edu.vinu.model.user_models.Teacher;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.service.common.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUserByEmail_shouldReturnUser_whenUserExists(){
        String email ="test@example.com";

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

    @Test
    void testGetUserByEmail_shouldThrowException_whenUserDoseNotExist(){
        String email = "missing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testGetUserByEmail_shouldThrowException_whenUserIsDisabled(){
        String email = "test@example.com";
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setEmail(email);
        mockUserEntity.setDisabled(true);

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(email));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testIsUserExistsByEmail_shouldReturnTrue_whenUserExists(){
        String email="test@example.com";
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(true);
        assertTrue(userService.isUserExist(email));
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void testIsUserExistsByEmail_shouldReturnFalse_whenUserDoseNotExists(){
        String email="test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        assertFalse(userService.isUserExist(email));
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void testIsUserDisabled_shouldReturnTrue_whenUserIsDisabled(){
        String email = "test@example.com";
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setEmail(email);
        mockUserEntity.setDisabled(true);
        when(userRepository.isUserDisabledByEmail(email)).thenReturn(true);
        assertTrue(userService.isUserDisabled(mockUserEntity));
        verify(userRepository).isUserDisabledByEmail(email);
    }

    @Test
    void testConvertToModel_withStudentEntity() {
        StudentEntity studentEntity = new StudentEntity();
        Student student = new Student();
        when(mapper.map(studentEntity, Student.class)).thenReturn(student);

        User result = userService.convertToModel(studentEntity);
        assertEquals(student, result);
    }

    @Test
    void testConvertToModel_withTeacherEntity() {
        TeacherEntity teacherEntity = new TeacherEntity();
        Teacher teacher = new Teacher();
        when(mapper.map(teacherEntity, Teacher.class)).thenReturn(teacher);

        User result = userService.convertToModel(teacherEntity);
        assertEquals(teacher, result);
    }

    @Test
    void testConvertToModel_withInstituteEntity() {
        InstituteEntity instituteEntity = new InstituteEntity();
        Institute institute = new Institute();
        when(mapper.map(instituteEntity, Institute.class)).thenReturn(institute);

        User result = userService.convertToModel(instituteEntity);
        assertEquals(institute, result);
    }

    @Test
    void testConvertToModel_withGenericUserEntity() {
        UserEntity userEntity = new UserEntity();
        User user = new User();
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        User result = userService.convertToModel(userEntity);
        assertEquals(user, result);
    }

    @Test
    void testConvertToModel_whenClassCastExceptionThrown() {
        UserEntity userEntity = new UserEntity();
        when(mapper.map(userEntity, User.class)).thenThrow(new ClassCastException("Mapping error"));

        InternalServerErrorException ex = assertThrows(InternalServerErrorException.class, () -> {
            userService.convertToModel(userEntity);
        });
        assertEquals("Mapping error", ex.getMessage());
    }

    @Test
    void testGetAllUsersByFirstNameLike_shouldReturnListOfTeachersAndStudents_whenUserFirstNameIsLike() {
        String firstName = "John";

        List<StudentEntity> mockStudentEntities = List.of(new StudentEntity(), new StudentEntity());
        List<TeacherEntity> mockTeacherEntities = List.of(new TeacherEntity(), new TeacherEntity());

        List<Student> expectedStudents = List.of(new Student(), new Student());
        List<Teacher> expectedTeachers = List.of(new Teacher(), new Teacher());

        List<User> expectedUsers = List.of(new Student(), new Teacher(), new Student(), new Teacher());

        when(userRepository.getStudentsByFirstNameLike(firstName)).thenReturn(mockStudentEntities);
        when(userRepository.getTeachersByFirstNameLike(firstName)).thenReturn(mockTeacherEntities);

        when(mapper.map(mockStudentEntities.get(0), Student.class)).thenReturn(expectedStudents.get(0));
        when(mapper.map(mockStudentEntities.get(1), Student.class)).thenReturn(expectedStudents.get(1));
        when(mapper.map(mockTeacherEntities.get(0), Teacher.class)).thenReturn(expectedTeachers.get(0));
        when(mapper.map(mockTeacherEntities.get(1), Teacher.class)).thenReturn(expectedTeachers.get(1));

        List<User> actualUsers = userService.getAllUsersByFirstNameLike(firstName);

        assertNotNull(actualUsers);

        assertEquals(expectedUsers.size(), actualUsers.size());

        verify(userRepository).getTeachersByFirstNameLike(firstName);
        verify(userRepository).getStudentsByFirstNameLike(firstName);
    }

    @Test
    void testGetAllUsersByFirstNameLike_shouldThrowUserNotFoundException_whenEmptyListOfUsers(){
        String firstName = "John";
        List<StudentEntity> mockStudentEntities = List.of();
        List<TeacherEntity> mockTeacherEntities = List.of();

        when(userRepository.getStudentsByFirstNameLike(firstName)).thenReturn(mockStudentEntities);
        when(userRepository.getTeachersByFirstNameLike(firstName)).thenReturn(mockTeacherEntities);

        assertThrowsExactly(UserNotFoundException.class, () -> userService.getAllUsersByFirstNameLike(firstName),"There are no users starts with "+firstName);

        verify(userRepository).getTeachersByFirstNameLike(firstName);
        verify(userRepository).getStudentsByFirstNameLike(firstName);
    }

    @Test
    void testGetAllStudentsByFirstNameLike_shouldReturnListOfStudents_whenUserFirstNameIsLike() {
        String firstName = "John";
        List<StudentEntity> mockStudentEntities = List.of(new StudentEntity(), new StudentEntity());
        List<Student> expectedStudents = List.of(new Student(), new Student());

        when(userRepository.getStudentsByFirstNameLike(firstName)).thenReturn(mockStudentEntities);

        for (int i = 0; i < mockStudentEntities.size(); i++) {
            when(mapper.map(mockStudentEntities.get(i), Student.class)).thenReturn(expectedStudents.get(i));
        }

        List<Student> actualStudents = userService.getAllStudentsByFirstNameLike(firstName);

        assertNotNull(actualStudents);
        assertEquals(expectedStudents.size(), actualStudents.size());

        verify(userRepository).getStudentsByFirstNameLike(firstName);
    }

    @Test
    void testGetAllStudentsByFirstNameLike_shouldReturnEmptyListOfStudents_whenUserFirstNameIsLike() {
        String firstName = "John";
        List<StudentEntity> mockStudentEntities = List.of();

        when(userRepository.getStudentsByFirstNameLike(firstName)).thenReturn(mockStudentEntities);

        List<Student> actualStudents = userService.getAllStudentsByFirstNameLike(firstName);

        assertNotNull(actualStudents);
        assertEquals(0, actualStudents.size());

        verify(userRepository).getStudentsByFirstNameLike(firstName);
    }

    @Test
    void testGetAllTeachersByFirstNameLike_shouldReturnListOfTeachers_whenUserFirstNameIsLike() {
        String firstName = "John";
        List<TeacherEntity> mockTeacherEntities = List.of(new TeacherEntity(), new TeacherEntity());
        List<Teacher> expectedTeachers = List.of(new Teacher(), new Teacher());

        when(userRepository.getTeachersByFirstNameLike(firstName)).thenReturn(mockTeacherEntities);

        for (int i = 0; i < mockTeacherEntities.size(); i++) {
            when(mapper.map(mockTeacherEntities.get(i), Teacher.class)).thenReturn(expectedTeachers.get(i));
        }

        List<Teacher> actualTeachers = userService.getAllTeachersByFirstNameLike(firstName);

        assertNotNull(actualTeachers);
        assertEquals(expectedTeachers.size(), actualTeachers.size());

        verify(userRepository).getTeachersByFirstNameLike(firstName);
    }

    @Test
    void testGetAllTeachersByFirstNameLike_shouldReturnEmptyListOfTeachers_whenUserFirstNameIsLike() {
        String firstName = "John";
        List<TeacherEntity> mockTeacherEntities = List.of();

        when(userRepository.getTeachersByFirstNameLike(firstName)).thenReturn(mockTeacherEntities);

        List<Teacher> actualTeachers = userService.getAllTeachersByFirstNameLike(firstName);

        assertNotNull(actualTeachers);
        assertEquals(0, actualTeachers.size());

        verify(userRepository).getTeachersByFirstNameLike(firstName);
    }

    @Test
    void testGetAllStudents_shouldReturnListOfAllStudents(){
        List<StudentEntity> mockStudentEntities = List.of(new StudentEntity(), new StudentEntity());
        List<Student> expectedStudents = List.of(new Student(), new Student());

        when(userRepository.getAllStudents()).thenReturn(mockStudentEntities);

        List<Student> actualStudents = userService.getAllStudents();

        assertNotNull(actualStudents);
        assertEquals(expectedStudents.size(), actualStudents.size());

        verify(userRepository).getAllStudents();
    }

    @Test
    void testGetAllStudents_ShouldThrowUserNotFoundException_whenNoStudentsFound(){
        List<StudentEntity> mockStudentEntities = List.of();

        when(userRepository.getAllStudents()).thenReturn(mockStudentEntities);

        assertThrowsExactly(UserNotFoundException.class, () ->{
            userService.getAllStudents();
        }, "No Students Found");

        verify(userRepository).getAllStudents();
    }

    @Test
    void testGetAllTeachers_shouldReturnListOfAllTeachers(){
        List<TeacherEntity> mockTeacherEntities = List.of(new TeacherEntity(), new TeacherEntity());
        List<Teacher> expectedTeachers = List.of(new Teacher(), new Teacher());

        when(userRepository.getAllTeachers()).thenReturn(mockTeacherEntities);

        List<Teacher> actualTeachers = userService.getAllTeachers();

        assertNotNull(actualTeachers);
        assertEquals(expectedTeachers.size(), actualTeachers.size());

        verify(userRepository).getAllTeachers();
    }

    @Test
    void testGetAllTeachers_ShouldThrowUserNotFoundException_whenNoTeachersFound(){
        List<TeacherEntity> mockTeacherEntities = List.of();

        when(userRepository.getAllTeachers()).thenReturn(mockTeacherEntities);

        assertThrowsExactly(UserNotFoundException.class, () ->{
            userService.getAllTeachers();
        }, "No Teachers Found!");

        verify(userRepository).getAllTeachers();
    }

    @Test
    void testGetAllInstitutes_shouldReturnListOfAllInstitutes(){
        List<InstituteEntity> mockInstituteEntities = List.of(new InstituteEntity(), new InstituteEntity());
        List<Institute> expectedInstitutes = List.of(new Institute(), new Institute());

        when(userRepository.getAllInstitutes()).thenReturn(mockInstituteEntities);

        List<Institute> actualInstitutes = userService.getAllInstitutes();

        assertNotNull(actualInstitutes);
        assertEquals(expectedInstitutes.size(), actualInstitutes.size());

        verify(userRepository).getAllInstitutes();
    }

    @Test
    void testGetAllInstitutes_ShouldThrowUserNotFoundException_whenNoInstitutesFound(){
        List<InstituteEntity> mockInstituteEntities = List.of();

        when(userRepository.getAllInstitutes()).thenReturn(mockInstituteEntities);

        assertThrowsExactly(UserNotFoundException.class, () ->{
            userService.getAllInstitutes();
        }, "No Institutes Found!");

        verify(userRepository).getAllInstitutes();
    }
}
