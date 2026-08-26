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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface BatchProjection {
    Long getId();
    Long getCourseId();
    String getName();
    Boolean getIsSeatLimited();
    Integer getMaxSeatLimit();
    LocalDate getStartDate();
    LocalTime getStartTime();
    String getBatchStatus();
    String getEnrollmentStatus();
    LocalDateTime getCreatedDate();
    LocalDateTime getLastModifiedDate();
}
