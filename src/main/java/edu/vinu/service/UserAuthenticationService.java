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

import edu.vinu.model.User;
import edu.vinu.request.UserLoginRequest;
import edu.vinu.request.UserRegistrationRequest;
import edu.vinu.response.AuthResponse;

public interface UserAuthenticationService {
    User registerUser(UserRegistrationRequest user);

    AuthResponse verify(UserLoginRequest request);
}
