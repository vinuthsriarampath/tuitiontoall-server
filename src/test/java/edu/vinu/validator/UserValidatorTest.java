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

package edu.vinu.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Test
    void testValidateEmail_success_returnsTrue(){
        assertTrue(UserValidator.isValidateEmail("test@email.com"));
    }

    @Test
    void testValidateEmail_failure_returnFalse(){
        assertFalse(UserValidator.isValidateEmail("test@"));
        assertFalse(UserValidator.isValidateEmail("test@email"));
        assertFalse(UserValidator.isValidateEmail("test@.com"));
        assertFalse(UserValidator.isValidateEmail(null));
        assertFalse(UserValidator.isValidateEmail("testemai.com"));
    }

    @Test
    void testValidatePassword_success_returnTrue(){
        assertTrue(UserValidator.isValidatePassword("Password@123"));
    }

    @Test
    void testValidatePassword_failure_returnFalse(){
        assertFalse(UserValidator.isValidatePassword(null));
        assertFalse(UserValidator.isValidatePassword("pass"));
        assertFalse(UserValidator.isValidatePassword("password"));
        assertFalse(UserValidator.isValidatePassword("PASSWORD"));
        assertFalse(UserValidator.isValidatePassword("Password"));
        assertFalse(UserValidator.isValidatePassword("Password@"));
    }

    @Test
    void testValidateDob_success_returnTrue(){
        assertTrue(UserValidator.isValidDob(LocalDate.now().minusYears(7)));
    }

    @Test
    void testValidateDob_failure_returnFalse(){
        assertThrowsExactly(DateTimeParseException.class,() -> UserValidator.isValidDob(LocalDate.parse("invalid-date")),"Invalid date format");
        assertFalse(UserValidator.isValidDob(LocalDate.now().plusYears(1)));
        assertFalse(UserValidator.isValidDob(LocalDate.now().plusMonths(1)));
        assertFalse(UserValidator.isValidDob(LocalDate.now().plusDays(1)));
        assertFalse(UserValidator.isValidDob(LocalDate.now()));
        assertFalse(UserValidator.isValidDob(LocalDate.now().minusYears(3)));
    }
}
