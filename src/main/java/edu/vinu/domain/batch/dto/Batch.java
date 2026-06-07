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

package edu.vinu.domain.batch.dto;

import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch {
    private Long id;
    private Long courseId;
    private String name;
    private Boolean is_seat_limited;
    private Integer max_seat_limit;
    private LocalDate start_date;
    private LocalTime start_time;
    private BatchStatus batch_status;
    private BatchEnrollmentStatus enrollment_status;
    private LocalDateTime created_date;
    private LocalDateTime last_modified_date;
}
