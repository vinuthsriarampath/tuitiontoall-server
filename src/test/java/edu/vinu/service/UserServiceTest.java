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

import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.exception.custom.UserNotFoundException;
import edu.vinu.model.user_models.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.service.common.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

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
        when(modelMapper.map(mockUserEntity, User.class)).thenReturn(expectedUser);

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
    void testIsUserExistsByEmail_shouldReturnFalse_whenUserDosentExists(){
        String email="test@example.com";
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setEmail(email);

        when(userRepository.existsByEmail(email)).thenReturn(true);
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
}
