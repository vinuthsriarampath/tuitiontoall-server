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

import edu.vinu.model.User;
import edu.vinu.request.UserLoginRequest;
import edu.vinu.request.UserRegistrationRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.AuthResponse;
import edu.vinu.service.UserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v2/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserAuthenticationService userAuthService;
    private final EmailValidator emailValidator;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UserRegistrationRequest user){
        user.setPassword(encoder.encode(user.getPassword()));
        User saveduser=userAuthService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse("User Registered Successfully!",saveduser));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        boolean validEmail = emailValidator.isValid(userLoginRequest.getEmail(),null);
        if(!validEmail){
            throw new IllegalStateException("Invalid Email Address!");
        }
        AuthResponse authResponse = userAuthService.verify(userLoginRequest);
        return ResponseEntity.ok(authResponse);
    }
}
