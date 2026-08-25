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

package edu.vinu.domain.batch.repository.projection;

import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface BatchDetailedProjection {
    Long getId();
    String getName();
    Long getCourseId();
    String getCourseTitle();
    boolean getIsSeatLimited();
    Integer getMaxSeatsLimit();
    Long getTotalEnrollments();
    BatchStatus getBatchStatus();
    BatchEnrollmentStatus getEnrollmentStatus();
    LocalDate getStartDate();
    LocalTime getStartTime();
    LocalDateTime getCreatedDate();
    LocalDateTime getLastModifiedDate();
}
