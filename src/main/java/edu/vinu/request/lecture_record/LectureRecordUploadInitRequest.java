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

package edu.vinu.request.lecture_record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record LectureRecordUploadInitRequest(
        @NotBlank(message = "Title is Mandatory!")
        String title,
        @NotNull(message = "Recorded Date is Mandatory!")
        @PastOrPresent(message = "Recorded Date cannot be in the future!")
        LocalDate recordedDate,
        @NotNull(message = "Chapter ID is Mandatory!")
        Long chapterId,
        @NotBlank(message = "Original File Name is Mandatory!")
        String originalFileName,
        @NotNull(message = "Total Size is Mandatory!")
        Long totalSize,
        @NotNull(message = "Total Chunks is Mandatory!")
        Integer totalChunks
) {
}
