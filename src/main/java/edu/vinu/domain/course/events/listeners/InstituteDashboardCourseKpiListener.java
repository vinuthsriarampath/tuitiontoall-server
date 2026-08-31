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

package edu.vinu.domain.course.events.listeners;

import edu.vinu.domain.course.events.CourseArchivedEvent;
import edu.vinu.domain.course.events.CourseCreatedEvent;
import edu.vinu.domain.course.events.CourseDeletedEvent;
import edu.vinu.domain.course.events.CourseUpdatedEvent;
import edu.vinu.domain.course.response.CourseMetricsUpdatedResponse;
import edu.vinu.domain.institute.service.InstituteDashboardKpiService;
import edu.vinu.domain.institute.service.InstituteDashboardWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InstituteDashboardCourseKpiListener {

    private final InstituteDashboardWebSocketService instituteDashboardWebSocketService;
    private final InstituteDashboardKpiService kpiService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CourseCreatedEvent event){
        updateCourseMetrics(event.course().getInstitute().getId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CourseUpdatedEvent event){
        updateCourseMetrics(event.instituteId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CourseDeletedEvent event){
        updateCourseMetrics(event.instituteId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CourseArchivedEvent event){
        updateCourseMetrics(event.instituteId());
    }

    private void updateCourseMetrics(Long instituteId){
        instituteDashboardWebSocketService.updateCourseMetrics(
                instituteId,
                CourseMetricsUpdatedResponse.builder()
                        .publishedCourses(kpiService.buildCourseKpi(instituteId))
                        .build());
    }

}
