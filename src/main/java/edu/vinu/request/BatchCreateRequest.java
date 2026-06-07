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

import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchCreateRequest {
    @NotNull(message = "Title is mandatory")
    @PositiveOrZero(message = "Course ID must be greater than or equal to zero")
    private Long courseId;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull(message = "State whether seats are limited or not")
    private Boolean is_seat_limited;
    @PositiveOrZero(message = "Max seat limit must be greater than or equal to zero")
    private Integer max_seat_limit;
    @NotNull(message = "Batch start date is mandatory")
    private LocalDate start_date;
    @NotNull(message = "Batch start time mandatory")
    private LocalTime start_time;
    @NotNull(message = "Batch status is mandatory")
    private BatchStatus batch_status;
    @NotNull(message = "Batch enrollment status is mandatory")
    private BatchEnrollmentStatus enrollment_status;
}
