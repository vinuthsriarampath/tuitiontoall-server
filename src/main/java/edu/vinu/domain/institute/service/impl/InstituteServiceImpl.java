/*
 * Copyright (c) 2026 vinuth sri arampath
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

package edu.vinu.domain.institute.service.impl;

import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.institute.service.InstituteService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstituteServiceImpl implements InstituteService {

    private final UserAuthenticationService  authenticationService;
    private final UserServiceImpl userService;

    @Override
    public InstituteEntity getCurrentInstitute() {
        UserEntity userEntity = userService.getUserEntityByEmail(authenticationService.getCurrentUserEmail());
        InstituteEntity instituteEntity =  userEntity.getInstitute();
        if(instituteEntity == null){
            throw new UnauthorizedException("You are not associated with any institute.");
        }
        return instituteEntity;
    }
}
