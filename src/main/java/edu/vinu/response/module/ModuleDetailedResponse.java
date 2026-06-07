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

package edu.vinu.response.module;

import edu.vinu.enums.ModuleStatus;
import edu.vinu.response.BatchBasicResponse;
import edu.vinu.domain.user.response.TeacherBasicResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ModuleDetailedResponse(
        Long id,
        String name,
        ModuleStatus status,
        BatchBasicResponse batch,
        TeacherBasicResponse teacher,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
