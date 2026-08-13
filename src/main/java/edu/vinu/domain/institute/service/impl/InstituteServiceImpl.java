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

import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.institute.mapper.InstituteMapper;
import edu.vinu.domain.institute.repository.InstituteRepository;
import edu.vinu.domain.institute.request.InstituteDetailsUpdateRequest;
import edu.vinu.domain.institute.service.InstituteService;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.mapper.UserMapper;
import edu.vinu.domain.user.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstituteServiceImpl implements InstituteService {

    private final UserAuthenticationService  authenticationService;
    private final InstituteRepository instituteRepository;
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

    @Override
    public List<Institute> getAllInstitutes() {
        List<Institute> instituteList =  instituteRepository.getAllInstitutes()
                .stream()
                .map(InstituteMapper::toInstitute)
                .toList();

        if (instituteList.isEmpty()){
            throw new NotFoundException("No Institutes Found!");
        }
        return instituteList;
    }

    @Override
    public List<User> getAllInstitutesByName(String instituteName) {
        return instituteRepository.findByInstituteName(instituteName)
                .stream()
                .map(instituteEntity -> UserMapper.toUser(instituteEntity.getUser(), InstituteMapper.toInstitute(instituteEntity)))
                .toList();
    }

    @Override
    public Institute updateInstituteDetails(String email, InstituteDetailsUpdateRequest instituteDetailsUpdateRequest) {
        UserEntity userEntity = userService.getUserEntityByEmail(email);

        userService.updateUserDetails(email, instituteDetailsUpdateRequest);

        userEntity.getInstitute().setInstituteName(instituteDetailsUpdateRequest.getInstituteName());

        return InstituteMapper.toInstitute(instituteRepository.save(userEntity.getInstitute()));
    }
}
