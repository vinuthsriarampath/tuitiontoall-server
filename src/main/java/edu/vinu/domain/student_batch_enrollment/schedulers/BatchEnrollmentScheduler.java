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

package edu.vinu.domain.student_batch_enrollment.schedulers;

import edu.vinu.domain.student_batch_enrollment.repository.StudentBatchEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchEnrollmentScheduler {
    private final StudentBatchEnrollmentRepository enrollmentRepository;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Colombo")
    public void updateEnrollmentStatuses() {
        int updatedCount = enrollmentRepository.completeEnrollmentsWhereBatchCompleted();
        log.atInfo().log("{} student batch enrollments updated to COMPLETED at {}", updatedCount, LocalDateTime.now());
    }

}
