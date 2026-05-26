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

package edu.vinu.request.schedule_lecture;

import edu.vinu.request.schedule_lecture.enums.ScheduleLectureCreateStatus;
import jakarta.validation.constraints.Future;
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
        @FutureOrPresent(message = "Meeting start Date must be a present or future date!")
        LocalDate startDate,
        @Future(message = "Meeting start time must be a future time!")
        LocalTime startTime,
        @Future(message = "Meeting end time must be a future time!")
        LocalTime endTime,
        @NotNull(message = "Late Attendance is mandatory!")
        boolean lateAttendance,
        @NotNull(message = "Meeting url is mandatory!")
        String meetingUrl,
        @NotNull(message = "Status is mandatory!")
        ScheduleLectureCreateStatus status
) {
}
