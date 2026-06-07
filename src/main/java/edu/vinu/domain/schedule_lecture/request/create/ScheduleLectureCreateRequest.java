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

package edu.vinu.domain.schedule_lecture.request.create;

import edu.vinu.domain.schedule_lecture.request.create.enums.ScheduleLectureCreateStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleLectureCreateRequest(
        @NotNull(message = "Chapter id is mandatory!")
        Long chapterId,
        @NotBlank(message = "Topic is mandatory!")
        String topic,
        @NotNull(message = "Meeting start Date is mandatory!")
        @FutureOrPresent(message = "Meeting start Date must be a present or future date!")
        LocalDate startDate,
        @NotNull(message = "Meeting start time is mandatory!")
        LocalTime startTime,
        @NotNull(message = "Meeting end time is mandatory!")
        LocalTime endTime,
        boolean lateAttendance,
        @NotBlank(message = "Meeting url is mandatory!")
        String meetingUrl,
        @NotNull(message = "Status is mandatory!")
        ScheduleLectureCreateStatus status
) {
}
