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

package edu.vinu.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVacancyRequest {
    @NotBlank(message = "Title is required!")
    private String title;

    @NotNull(message = "required Years of Experience is mandatory!")
    private Integer requiredExperienceYears;

    @NotBlank(message = "Job description is mandatory!")
    private String jobDescription;

    @NotNull(message = "Vacancy closing date is mandatory!")
    @Future(message = "Vacancy closing date must be in the future!")
    private LocalDateTime vacancyClosingDate;
}
