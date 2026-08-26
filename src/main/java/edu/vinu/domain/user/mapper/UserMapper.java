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

package edu.vinu.domain.user.mapper;

import edu.vinu.domain.role.dto.RoleDetails;
import edu.vinu.domain.role.mapper.RoleMapper;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.user.entity.UserEntity;

public class UserMapper {
    public static User toUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .address(userEntity.getAddress())
                .contact(userEntity.getContact())
                .email(userEntity.getEmail())
                .role(RoleMapper.toRole(userEntity.getRole()))
                .isDisabled(userEntity.isDisabled())
                .userSlug(userEntity.getUserSlug())
                .dp(userEntity.getDp())
                .banner(userEntity.getBanner())
                .creationTimeStamp(userEntity.getCreationTimeStamp())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    public static User toUser(UserEntity userEntity, RoleDetails details){
        User user = toUser(userEntity);
        user.setDetails(details);
        return user;
    }
}
