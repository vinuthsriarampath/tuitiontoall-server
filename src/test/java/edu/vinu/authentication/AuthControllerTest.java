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

package edu.vinu.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vinu.entity.user_entities.InstituteEntity;
import edu.vinu.entity.user_entities.UserEntity;
import edu.vinu.repository.UserRepository;
import edu.vinu.request.UserLoginRequest;
import edu.vinu.request.registration.InstituteRegistrationRequest;
import edu.vinu.service.auth.UserAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserLoginRequest loginRequest;

    private InstituteRegistrationRequest getValidInstituteRequest() {
        InstituteRegistrationRequest request = new InstituteRegistrationRequest();
        request.setInstituteName("Test Institute");
        request.setAddress("123 Main Street");
        request.setContact("0000000000");
        request.setEmail("institute@email.com");
        request.setPassword("Password@123");
        return request;
    }

//    @Test
//    void registerInstitute_success() throws Exception {
//        InstituteRegistrationRequest instituteRegistrationRequest = getValidInstituteRequest();
//        // Perform the POST request
//        mockMvc.perform(post("/api/v2/auth/register/institute")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(instituteRegistrationRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Institute Registered Successfully!"))
//                .andExpect(jsonPath("$.data.email").value(instituteRegistrationRequest.getEmail()))
//                .andExpect(jsonPath("$.data.instituteName").value(instituteRegistrationRequest.getInstituteName()))
//                .andExpect(jsonPath("$.data.address").value(instituteRegistrationRequest.getAddress()))
//                .andExpect(jsonPath("$.data.contact").value(instituteRegistrationRequest.getContact()));
//
//        UserEntity savedUser = userRepository.findByEmail("institute@email.com");
//
//        assertThat(savedUser).isNotNull();
//        assertThat(savedUser.getEmail()).isEqualTo(instituteRegistrationRequest.getEmail());
//        assertThat(savedUser.getRole()).isEqualTo(Role.ROLE_INSTITUTE);
//        assertThat(savedUser).isInstanceOf(InstituteEntity.class);
//
//        InstituteEntity instituteEntity = (InstituteEntity) savedUser;
//
//        assertThat(instituteEntity.getInstituteName()).isEqualTo(instituteRegistrationRequest.getInstituteName());
//        assertThat(savedUser.getAddress()).isEqualTo(instituteRegistrationRequest.getAddress());
//        assertThat(savedUser.getContact()).isEqualTo(instituteRegistrationRequest.getContact());
//        assertThat(savedUser.getUserSlug()).isNotNull();
//    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""," "})
    void registerInstitute_FailedValidation_WhenBlankNullEmptyInstituteName(String invalidName) throws Exception {
        InstituteRegistrationRequest instituteRegistrationRequest = getValidInstituteRequest();
        instituteRegistrationRequest.setInstituteName(invalidName);

        mockMvc.perform(post("/api/v2/auth/register/institute")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(instituteRegistrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User Validation Failed"))
                .andExpect(jsonPath("$.data.instituteName").value("Institute Name cannot be blank"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""," "})
    void registerInstitute_FailedValidation_WhenBlankNullEmptyAddress(String invalidAddress) throws Exception {
        InstituteRegistrationRequest instituteRegistrationRequest = getValidInstituteRequest();
        instituteRegistrationRequest.setAddress(invalidAddress);

        mockMvc.perform(post("/api/v2/auth/register/institute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instituteRegistrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User Validation Failed"))
                .andExpect(jsonPath("$.data.address").value("Address cannot be blank"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""," "})
    void registerInstitute_FailedValidation_WhenContactNullOrBlank(String invalidContact) throws Exception {
        InstituteRegistrationRequest instituteRegistrationRequest = getValidInstituteRequest();
        instituteRegistrationRequest.setContact(invalidContact);

        mockMvc.perform(post("/api/v2/auth/register/institute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instituteRegistrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User Validation Failed"))
                .andExpect(jsonPath("$.data.contact").value("Contact cannot be blank"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd","00000000","000000000"})
    void registerInstitute_FailedValidation_WhenContactLessOrMoreThanTenCharacters(String invalidContact) throws Exception {
        InstituteRegistrationRequest instituteRegistrationRequest = getValidInstituteRequest();
        instituteRegistrationRequest.setContact(invalidContact);

        mockMvc.perform(post("/api/v2/auth/register/institute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instituteRegistrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User Validation Failed"))
                .andExpect(jsonPath("$.data.contact").value("Contact must be 10 digits long"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""," "})
    void registerInstitute_FailedValidation_WhenBlankNullEmptyEmail(String invalidEmail) throws Exception {
        InstituteRegistrationRequest request = getValidInstituteRequest();
        request.setEmail(invalidEmail);

        mockMvc.perform(post("/api/v2/auth/register/institute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User Validation Failed"))
                .andExpect(jsonPath("$.data.email").value("Email cannot be blank"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""," "})
    void registerInstitute_FailedValidation_WhenBlankNullEmptyPassword(String invalidPassword) throws Exception {
        InstituteRegistrationRequest instituteRegistrationRequest = getValidInstituteRequest();
        instituteRegistrationRequest.setPassword(invalidPassword);

        mockMvc.perform(post("/api/v2/auth/register/institute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(instituteRegistrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User Validation Failed"))
                .andExpect(jsonPath("$.data.password").value("Password cannot be null"));
    }
}
