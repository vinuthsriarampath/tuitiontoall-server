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

import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.batch.request.BatchCreateRequest;
import edu.vinu.domain.batch.service.BatchService;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.course.events.CourseCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultBatchCreationEventListener {

    private final BatchService batchService;

    @EventListener
    public void handle(CourseCreatedEvent event) {
        CourseEntity course = event.course();

        batchService.createBatch(event.course(), new BatchCreateRequest(
                course.getId(),
                "DEFAULT-" + course.getId(),
                false,
                0,
                course.getCreationTimeStamp().toLocalDate().plusDays(1),
                course.getCreationTimeStamp().toLocalTime(),
                BatchStatus.PREPARATION,
                BatchEnrollmentStatus.OPEN
        ));
    }
}
