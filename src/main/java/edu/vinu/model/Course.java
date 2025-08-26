/*
 * Copyright (c) 2025 vinuth sri arampath
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

package edu.vinu.model;

import edu.vinu.enums.*;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Nullable
    private Long id;
    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @NotBlank(message = "Duration In Hours is mandatory")
    @Positive(message = "Duration In Hours must be greater than zero ")
    private int durationInHours;
    @NotBlank(message = "Price is mandatory")
    @PositiveOrZero(message = "Price must be greater than or equal to zero ")
    private Double price;
    private CourseLevel level;
    @NotBlank(message = "Category is mandatory")
    private CourseCategory category;
    @NotBlank(message = "Status is mandatory")
    private CourseStatus status;
    @NotBlank(message = "Language is mandatory")
    private CourseLanguage language;
    @NotBlank(message = "Mode is mandatory")
    private CourseMode mode;
}
