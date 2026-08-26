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

package edu.vinu.domain.batch.response;

import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record BatchDetailedResponse(
        Long id,
        String name,
        Long courseId,
        String courseTitle,
        Long totalEnrollments,
        boolean isSeatLimited,
        Integer maxSeatsLimit,
        BatchStatus batchStatus,
        BatchEnrollmentStatus enrollmentStatus,
        LocalDate startDate,
        LocalTime startTime,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
