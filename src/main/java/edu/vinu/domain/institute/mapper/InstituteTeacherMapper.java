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

package edu.vinu.domain.institute.mapper;

import edu.vinu.domain.institute.repository.projection.InstituteTeacherProjection;
import edu.vinu.domain.institute.response.InstituteTeacherResponse;
import edu.vinu.domain.teacher.dtos.response.TeacherUserResponse;

public class InstituteTeacherMapper {

    public static InstituteTeacherResponse toInstituteTeacherResponse(InstituteTeacherProjection p) {
        return InstituteTeacherResponse.builder()
                .id(p.getId())
                .instituteId(p.getInstituteId())
                .status(p.getStatus())
                .teacher(TeacherUserResponse.builder()
                        .userId(p.getUserId())
                        .email(p.getEmail())
                        .contact(p.getContact())
                        .dp(p.getDp())
                        .address(p.getAddress())
                        .firstName(p.getFirstName())
                        .lastName(p.getLastName())
                        .dob(p.getDob())
                        .build()
                ).joinedDate(p.getJoinedDate())
                .lastModifiedDate(p.getLastModifiedDate())
                .build();
    }
}
