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

package edu.vinu.domain.lecture_record.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record LectureRecordDetailsUpdateRequest(
        @NotBlank(message = "Title is Mandatory!")
        String title,
        @NotNull(message = "Recorded Date is Mandatory!")
        @PastOrPresent(message = "Recorded Date cannot be in the future!")
        LocalDate recordedDate
) {
}
