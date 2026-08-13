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

import edu.vinu.domain.user.dto.Role;
import edu.vinu.domain.user.entity.RoleEntity;

public class RoleMapper {
    public static Role toRole(RoleEntity roleEntity) {
        return Role.builder()
                .id(roleEntity.getId())
                .role(roleEntity.getRole())
                .creationTimeStamp(roleEntity.getCreationTimeStamp())
                .updatedAt(roleEntity.getUpdatedAt())
                .build();
    }
}
