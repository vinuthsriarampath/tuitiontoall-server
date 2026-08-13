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

package edu.vinu.domain.user.service;

import edu.vinu.domain.institute.dto.Institute;
import edu.vinu.domain.institute.request.InstituteDetailsUpdateRequest;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.request.update.UserDetailsUpdateRequest;

import java.util.List;

public interface UserService {
    User getUserByEmail(String email);

    UserEntity getUserEntityByEmail(String email);

    UserEntity getUserEntityById(Long id);

    boolean isUserExist(String email);

    User updateUserDetails(String email, UserDetailsUpdateRequest userUpdateRequest);

    List<Object> getAllUsersByFirstNameLike(String firstname);












    Institute updateInstituteDetails(String currentEmail, InstituteDetailsUpdateRequest instituteDetailsUpdateRequest);

    String generateUserSlug(String base);





    void disableUserAccountByEmail(String email);

    boolean isUserDisabled(UserEntity userEntity);

    User getUserByUserSlug(String userSlug);
}
