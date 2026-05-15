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

package edu.vinu.response;

import edu.vinu.enums.BatchEnrollmentStatus;
import edu.vinu.enums.BatchStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BatchBasicResponse(
        Long id,
        Long courseId,
        String name,
        BatchStatus status,
        BatchEnrollmentStatus enrollmentStatus,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
