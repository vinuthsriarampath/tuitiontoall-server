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

import edu.vinu.request.grading_range.GradingRangeCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AssignmentCreateRequest(

        @NotBlank(message = "Assignment topic is mandatory!")
        String topic,

        String description,

        @NotNull(message = "Total mark is mandatory!")
        Integer totalMarks,

        @NotNull(message = "File name is mandatory!")
        String fileName,

        @NotNull(message = "Assignment public available date is mandatory!")
        @FutureOrPresent(message = "Assignment public available date must be in the present or future!")
        LocalDateTime availableOn,

        @NotNull(message = "Assignment due date is mandatory!")
        @Future(message = "Assignment due date must be in the future!")
        LocalDateTime dueDate,

        boolean lateSubmission,
        boolean resubmission,

        @NotNull(message = "Max attempts is mandatory!")
        @Min(value = 1, message = "Minimum value for Max attempts is 1!")
        Integer maxAttempts,

        @Valid
        @NotNull(message = "Grading ranges list is mandatory!")
        @NotEmpty(message = "At least one grading range is required!")
        List<GradingRangeCreateRequest> gradingRanges
) {
}
