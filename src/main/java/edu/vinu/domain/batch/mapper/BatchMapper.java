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

package edu.vinu.domain.batch.mapper;

import edu.vinu.domain.batch.dto.Batch;
import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.batch.repository.projection.BatchDetailedProjection;
import edu.vinu.domain.batch.repository.projection.BatchProjection;
import edu.vinu.domain.batch.response.BatchDetailedResponse;

public class BatchMapper {

    public static Batch toBatch(BatchProjection p) {
        return Batch.builder()
                .id(p.getId())
                .courseId(p.getCourseId())
                .name(p.getName())
                .is_seat_limited(p.getIsSeatLimited())
                .max_seat_limit(p.getMaxSeatLimit())
                .start_date(p.getStartDate())
                .start_time(p.getStartTime())
                .batch_status(BatchStatus.valueOf(p.getBatchStatus()))
                .enrollment_status(BatchEnrollmentStatus.valueOf(p.getEnrollmentStatus()))
                .created_date(p.getCreatedDate())
                .last_modified_date(p.getLastModifiedDate())
                .build();
    }

    public static BatchDetailedResponse toBatchDetailedResponse(BatchDetailedProjection p) {
        return BatchDetailedResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .courseId(p.getCourseId())
                .courseTitle(p.getCourseTitle())
                .isSeatLimited(p.getIsSeatLimited())
                .maxSeatsLimit(p.getMaxSeatsLimit())
                .totalEnrollments(p.getTotalEnrollments())
                .batchStatus(p.getBatchStatus())
                .enrollmentStatus(p.getEnrollmentStatus())
                .startDate(p.getStartDate())
                .startTime(p.getStartTime())
                .createdDate(p.getCreatedDate())
                .lastModifiedDate(p.getLastModifiedDate())
                .build();
    }
}
