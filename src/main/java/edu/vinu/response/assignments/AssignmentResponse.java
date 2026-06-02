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

package edu.vinu.response.assignments;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AssignmentResponse(
        Long id,
        String topic,
        String description,
        String fileName,
        Integer totalMarks,
        LocalDateTime availableOn,
        LocalDateTime dueDate,
        boolean lateSubmission,
        boolean reSubmission,
        Integer maxAttempts,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
