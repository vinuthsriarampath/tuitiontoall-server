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

package edu.vinu.domain.assignment.request.chapter_assignments;

import java.time.LocalDateTime;

public record ChapterAssignmentFilterRequest(
        Long assignmentId,
        String topic,
        Boolean resubmission,
        Boolean lateSubmission,
        Integer totalMarks,
        Integer maxAttempts,
        LocalDateTime availableOn,
        LocalDateTime dueDate,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
