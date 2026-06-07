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

package edu.vinu.request.assignments;

import edu.vinu.domain.grading.request.GradingRangeUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record AssignmentUpdateRequest(

        @NotBlank(message = "Assignment topic is mandatory!")
        String topic,

        String description,

        @NotNull(message = "Total mark is mandatory!")
        Integer totalMarks,

        @NotNull(message = "Assignment public available date is mandatory!")
        LocalDateTime availableOn,

        @NotNull(message = "Assignment due date is mandatory!")
        LocalDateTime dueDate,

        boolean lateSubmission,
        boolean resubmission,

        @NotNull(message = "Max attempts is mandatory!")
        @Min(value = 1, message = "Minimum value for Max attempts is 1!")
        Integer maxAttempts,

        @Valid
        @NotNull(message = "Grading ranges list is mandatory!")
        @NotEmpty(message = "At least one grading range is required!")
        List<GradingRangeUpdateRequest> gradingRanges
) {
}
