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

package edu.vinu.domain.review.request;

import jakarta.validation.constraints.*;

public record ReviewCreateRequest(
        @NotBlank(message = "Review cannot be blank")
        String review,
        @NotNull(message = "Rating cannot be null")
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        int rating,
        @NotNull(message = "Course ID cannot be null")
        @Positive(message = "Course ID must be a positive number")
        Long courseId
) {
}
