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

package edu.vinu.domain.schedule_lecture.mapper;

import edu.vinu.domain.schedule_lecture.entity.ScheduleLectureEntity;
import edu.vinu.domain.schedule_lecture.response.ScheduleLectureResponse;
import org.springframework.stereotype.Component;

@Component
public class ScheduleLectureMapper {

    public static ScheduleLectureResponse toScheduleLectureResponse(ScheduleLectureEntity entity) {
        return ScheduleLectureResponse.builder()
                .id(entity.getId())
                .chapterId(entity.getChapter().getId())
                .topic(entity.getTopic())
                .startDate(entity.getStartDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .lateAttendance(entity.isLateAttendance())
                .meetingUrl(entity.getMeetingUrl())
                .status(entity.getStatus())
                .createdDate(entity.getCreatedDate())
                .lastModifiedDate(entity.getLastModifiedDate())
                .build();

    }
}
