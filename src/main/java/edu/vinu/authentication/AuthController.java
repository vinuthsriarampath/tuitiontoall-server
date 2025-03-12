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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/v2/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserAuthenticationService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserRegistrationRequest user,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponse("Validation Failed", errors));
        }
        User saveduser=userAuthService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse("User Registered Successfully!",saveduser));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest){
        AuthResponse authResponse = userAuthService.verify(userLoginRequest);
        return ResponseEntity.ok(authResponse);
    }
}
