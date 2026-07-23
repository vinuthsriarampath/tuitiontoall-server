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

package edu.vinu.domain.schedule_lecture.schedulers;

import edu.vinu.domain.schedule_lecture.enums.ScheduleLectureStatus;
import edu.vinu.domain.schedule_lecture.repository.ScheduleLectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleLectureSchedulers {
    private final ScheduleLectureRepository scheduleLectureRepository;

    @Scheduled(cron = " 0 * * * * *", zone = "Asia/Colombo")
    private void completeEndedLectures(){
        int updated = scheduleLectureRepository.completeEndedLectures(
                ScheduleLectureStatus.COMPLETED.name(),
                ScheduleLectureStatus.LIVE.name(),
                ScheduleLectureStatus.SCHEDULED.name()
        );
        log.atInfo().log("{} Scheduled Lectures got COMPLETED! at {}", updated , LocalDateTime.now());
    }
}
