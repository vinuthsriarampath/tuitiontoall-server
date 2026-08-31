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

package edu.vinu.domain.batch.events.listeners;

import edu.vinu.domain.batch.events.BatchCreatedEvent;
import edu.vinu.domain.batch.events.BatchUpdateEvent;
import edu.vinu.domain.batch.response.BatchMetricsUpdatedResponse;
import edu.vinu.domain.institute.service.InstituteDashboardKpiService;
import edu.vinu.domain.institute.service.InstituteDashboardWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InstituteDashboardBatchKpiListener {

    private final InstituteDashboardWebSocketService dashboardWebSocketService;
    private final InstituteDashboardKpiService kpiService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BatchCreatedEvent event) {
        updateBatchKpiMetrics(event.instituteId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BatchUpdateEvent event){
        updateBatchKpiMetrics(event.instituteId());
    }

    private void updateBatchKpiMetrics(Long instituteId){
        dashboardWebSocketService.updateBatchMetrics(
                instituteId,
                BatchMetricsUpdatedResponse.builder()
                        .ongoingBatches(kpiService.buildBatchKpi(instituteId))
                        .build()
        );
    }
}
