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

package edu.vinu.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User{
    @JsonProperty(index = 1)
    private Long id;
    private String address;
    private String contact;
    private String email;
    @JsonIgnore
    private String password;
    private Role role;
    private boolean isDisabled;
    private String userSlug;
    private String dp;
    private String banner;
    private LocalDateTime creationTimeStamp;
    private LocalDateTime updatedAt;

    private RoleDetails details;
}
