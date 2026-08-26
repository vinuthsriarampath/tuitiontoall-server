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

package edu.vinu.domain.batch.schedulers;

import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.batch.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final BatchRepository batchRepository;

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Colombo")
    private void closeEnrollmentAndStartPreparationBatch(){
        int updated = batchRepository.closeEnrollmentAndStartPreparationBatch(
                BatchEnrollmentStatus.OPEN.name(),
                BatchEnrollmentStatus.CLOSED.name(),
                BatchStatus.PREPARATION.name(),
                BatchStatus.ONGOING.name()
        );
        log.atInfo().log("{} PREPARATION batches got ONGOING! and CLOSE Enrollment at {}", updated , LocalDateTime.now());
    }
}
