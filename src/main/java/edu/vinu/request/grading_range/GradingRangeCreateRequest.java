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

package edu.vinu.request.grading_range;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GradingRangeCreateRequest(

        @NotNull(message = "Minimum mark is mandatory!")
        @Min(value = 0, message = "Minimum mark cannot be negative!")
        Integer minMarks,

        @NotNull(message = "Maximum mark is mandatory!")
        @Min(value = 0, message = "Maximum mark cannot be negative!")
        Integer maxMarks,

        @NotBlank(message = "Desired grade is mandatory!")
        @Size(max = 5, message = "Desired grade cannot exceed 5 characters!")
        String desiredGrade,

        String description
) {}
