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

package edu.vinu.domain.institute.event;

import edu.vinu.domain.institute.response.InstituteTeacherMatricsUpdatedResponse;
import edu.vinu.domain.institute.service.InstituteDashboardKpiService;
import edu.vinu.domain.institute.service.InstituteDashboardWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TeacherOnboardedEventListener {
    private final InstituteDashboardWebSocketService instituteDashboardWebSocketService;
    private final InstituteDashboardKpiService instituteDashboardKpiService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTeacherOnboardEvent(TeacherOnboardedEvent event){
        instituteDashboardWebSocketService.updateTeacherMetrics(
                event.instituteId(),
                InstituteTeacherMatricsUpdatedResponse.builder()
                        .activeTeachers(
                                instituteDashboardKpiService.buildTeacherKpi(event.instituteId())
                        ).build()
        );
    }

}
