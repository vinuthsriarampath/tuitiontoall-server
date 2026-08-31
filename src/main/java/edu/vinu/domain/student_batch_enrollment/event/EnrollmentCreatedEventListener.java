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

package edu.vinu.domain.student_batch_enrollment.event;

import edu.vinu.domain.institute.service.InstituteDashboardKpiService;
import edu.vinu.domain.institute.service.InstituteDashboardWebSocketService;
import edu.vinu.domain.student_batch_enrollment.dto.respose.EnrollmentMetricsUpdatedResponse;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnrollmentCreatedEventListener {

    private final EnrollmentService enrollmentService;
    private final InstituteDashboardKpiService instituteDashboardKpiService;
    private final InstituteDashboardWebSocketService instituteDashboardWebSocketService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEnrollmentCreated(EnrollmentCreatedEvent event) {
        EnrollmentMetricsUpdatedResponse response = new EnrollmentMetricsUpdatedResponse(
                instituteDashboardKpiService.buildStudentKpi(event.instituteId()),
                instituteDashboardKpiService.buildRevenueKpi(event.instituteId()),
                enrollmentService.getOverallEnrollmentStatsByInstitute(event.instituteId(), event.instituteCreatedDate()),
                enrollmentService.getEnrollmentDistributionByInstitute(event.instituteId())
        );

        instituteDashboardWebSocketService.updateEnrollmentMetrics(event.instituteId(), response);
    }
}
