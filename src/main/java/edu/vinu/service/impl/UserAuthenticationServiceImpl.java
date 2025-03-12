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

import edu.vinu.entity.UserEntity;
import edu.vinu.exception.InvalidInputException;
import edu.vinu.exception.UserAlreadyExistException;
import edu.vinu.model.AuthenticatedUserData;
import edu.vinu.model.User;
import edu.vinu.repository.UserRepository;
import edu.vinu.request.UserLoginRequest;
import edu.vinu.request.UserRegistrationRequest;
import edu.vinu.response.AuthResponse;
import edu.vinu.service.UserAuthenticationService;
import edu.vinu.service.UserService;
import edu.vinu.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User registerUser(UserRegistrationRequest request) {

        if (validateEmail(request.getEmail())){
            throw new InvalidInputException("Invalid email");
        }
        if (!validatePassword(request.getPassword())){
            throw new InvalidInputException("Invalid password");
        }else{
            request.setPassword(encoder.encode(request.getPassword()));
        }
        if (!isValidDOB(request.getDob())){
            throw new InvalidInputException("Invalid date of birth");
        }

        UserEntity userEntity = mapper.map(request, UserEntity.class);

        if (userService.isUserExist(request.getEmail())){
            throw new UserAlreadyExistException();
        }

        UserEntity savedUserEntity= userRepository.save(userEntity);
        return mapper.map(savedUserEntity, User.class);
    }

    @Override
    public AuthResponse verify(UserLoginRequest request){
        if (UserValidator.validateEmail(request.getEmail())){
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
