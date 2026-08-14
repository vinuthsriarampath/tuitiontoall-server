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

package edu.vinu.domain.student.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record StudentUserResponse(
        Long studentId,
        String firstName,
        String lastName,
        LocalDate dob,

        Long userId,
        String email,
        String contact,
        String dp,
        String address
) {
}
