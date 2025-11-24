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

package edu.vinu.request;


import edu.vinu.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateRequest {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Duration In Hours is mandatory")
    @Positive(message = "Duration In Hours must be greater than zero")
    private Integer durationInHours;

    @NotNull(message = "Price is mandatory")
    @PositiveOrZero(message = "Price must be greater than or equal to zero")
    private Double price;

    @NotNull(message = "Level is mandatory")
    private CourseLevel level;

    @NotNull(message = "Category is mandatory")
    private CourseCategory category;

    @NotNull(message = "Status is mandatory")
    private CourseStatus status;

    @NotNull(message = "Language is mandatory")
    private CourseLanguage language;

    @NotNull(message = "Mode is mandatory")
    private CourseMode mode;
}
