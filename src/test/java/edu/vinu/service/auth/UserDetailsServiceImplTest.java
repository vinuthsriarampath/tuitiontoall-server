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
import edu.vinu.model.UserPrinciple;
import edu.vinu.repository.UserRepository;
import edu.vinu.service.auth.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserPrinciple userPrinciple;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private final InstituteEntity instituteEntity= new InstituteEntity();
    private final TeacherEntity teacherEntity= new TeacherEntity();
    private final StudentEntity studentEntity= new StudentEntity();

//    @BeforeEach
//    void setup(){
//        instituteEntity.setInstituteName("Test Institute");
//        instituteEntity.setAddress("123 Main St");
//        instituteEntity.setContact("1234567890");
//        instituteEntity.setEmail("institute@test.com");
//        instituteEntity.setRole(Role.ROLE_INSTITUTE);
//        instituteEntity.setPassword("Password@123");
//
//        teacherEntity.setFirstName("John");
//        teacherEntity.setLastName("Doe");
//        teacherEntity.setDob(LocalDate.of(2000, 1, 1));
//        teacherEntity.setAddress("123 Main St");
//        teacherEntity.setContact("1234567890");
//        teacherEntity.setEmail("student@test.com");
//        teacherEntity.setRole(Role.ROLE_TEACHER);
//        teacherEntity.setPassword("Password@123");
//
//        studentEntity.setFirstName("Jane");
//        studentEntity.setLastName("Doe");
//        studentEntity.setDob(LocalDate.of(1980, 1, 1));
//        studentEntity.setAddress("123 Main St");
//        studentEntity.setContact("1234567890");
//        studentEntity.setRole(Role.ROLE_STUDENT);
//        studentEntity.setEmail("teacher@test.com");
//        studentEntity.setPassword("Password@123");
//    }

//    @Test
//    void testLoadUserByUsername_success_shouldReturnInstituteUserDetails(){
//
//        when(userRepository.findByEmail(instituteEntity.getEmail())).thenReturn(instituteEntity);
//
//        UserDetails actual = userDetailsService.loadUserByUsername(instituteEntity.getEmail());
//
//        assertEquals(instituteEntity.getEmail(), actual.getUsername());
//        assertEquals(instituteEntity.getPassword(), actual.getPassword());
//        assertEquals(instituteEntity.getRole().toString(), actual.getAuthorities().iterator().next().getAuthority());
//        verify(userRepository, times(1)).findByEmail(instituteEntity.getEmail());
//    }

//    @Test
//    void testLoadUserByUsername_success_shouldReturnTeacherUserDetails(){
//
//        when(userRepository.findByEmail(teacherEntity.getEmail())).thenReturn(teacherEntity);
//
//        UserDetails actual = userDetailsService.loadUserByUsername(teacherEntity.getEmail());
//
//        assertEquals(teacherEntity.getEmail(), actual.getUsername());
//        assertEquals(teacherEntity.getPassword(), actual.getPassword());
//        assertEquals(teacherEntity.getRole().toString(), actual.getAuthorities().iterator().next().getAuthority());
//        verify(userRepository, times(1)).findByEmail(teacherEntity.getEmail());
//    }

//    @Test
//    void testLoadUserByUsername_success_shouldReturnStudentUserDetails(){
//
//        when(userRepository.findByEmail(studentEntity.getEmail())).thenReturn(studentEntity);
//
//        UserDetails actual = userDetailsService.loadUserByUsername(studentEntity.getEmail());
//
//        assertEquals(studentEntity.getEmail(), actual.getUsername());
//        assertEquals(studentEntity.getPassword(), actual.getPassword());
//        assertEquals(studentEntity.getRole().toString(), actual.getAuthorities().iterator().next().getAuthority());
//        verify(userRepository, times(1)).findByEmail(studentEntity.getEmail());
//    }

//    @Test
//    void testLoadUserByUsername_failed_shouldThrowUsernameNotFoundException(){
//
//        when(userRepository.findByEmail(instituteEntity.getEmail())).thenReturn(null);
//
//        assertThrowsExactly(UsernameNotFoundException.class,() -> userDetailsService.loadUserByUsername(instituteEntity.getEmail()));
//    }
}
