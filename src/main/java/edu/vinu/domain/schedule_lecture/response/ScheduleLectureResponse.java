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

package edu.vinu.domain.schedule_lecture.response;

import edu.vinu.domain.schedule_lecture.enums.ScheduleLectureStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record ScheduleLectureResponse (
        Long id,
        Long chapterId,
        String topic,
        LocalDate startDate,
        LocalTime startTime,
        LocalTime endTime,
        boolean lateAttendance,
        String meetingUrl,
        ScheduleLectureStatus status,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
){
}
