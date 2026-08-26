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

package edu.vinu.domain.teacher_vacancy.schedulers;

import edu.vinu.domain.teacher_vacancy.enums.TeacherVacancyStatus;
import edu.vinu.domain.teacher_vacancy.repository.TeacherVacancyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeacherVacancyScheduler {
    private final TeacherVacancyRepository teacherVacancyRepository;

    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Colombo")
    private void closeVacancies(){
        int updated = teacherVacancyRepository.closeExpiredVacancies(
                TeacherVacancyStatus.OPEN.name(),
                TeacherVacancyStatus.CLOSED.name()
        );

        log.info("{} Vacancies got CLOSED! at {}", updated, LocalDateTime.now());
    }
}
