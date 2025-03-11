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

package edu.vinu.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vinu.entity.UserEntity;
import edu.vinu.model.AuthenticatedUserData;
import edu.vinu.model.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.request.UserLoginRequest;
import edu.vinu.request.UserRegistrationRequest;
import edu.vinu.response.AuthResponse;
import edu.vinu.service.UserAuthenticationService;
import edu.vinu.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private final AuthenticationManager authManager;

    private final UserRepository userDetailsRepository;

    private final JwtService jwtService;

    private final UserService userService;

    private final ModelMapper mapper;

    @Override
    public User registerUser(UserRegistrationRequest user) {
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        UserEntity savedUserEntity= userDetailsRepository.save(userEntity);
        return mapper.map(savedUserEntity, User.class);
    }

    @Override
    public AuthResponse verify(UserLoginRequest request){
        Authentication authentication = authManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        if (authentication.isAuthenticated()){
            String token = jwtService.generateToken(authentication);
            User user = userService.getUserByEmail(request.getEmail());
            return new AuthResponse(
                    token,
                    new AuthenticatedUserData(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getDob(),
                            user.getAddress(),
                            user.getContact(),
                            user.getEmail(),
                            user.getRole(),
                            user.isDisabled()
                    )
                    );
        }
        throw new IllegalStateException("Invalid access");
    }
}
