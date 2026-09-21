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

package edu.vinu.domain.course.repository.projections;

import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.course.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface StudentCourseProjection {
    Long getCourseId();
    String getCourseTitle();
    String getCourseDescription();
    Integer getDurationInHours();
    Double getPrice();

    CourseLevel getCourseLevel();
    CourseCategory getCourseCategory();
    CourseStatus getCourseStatus();
    CourseLanguage getCourseLanguage();
    CourseMode getCourseMode();

    String getThumbnail();

    BigDecimal getAvgRating();
    Integer getTotalRatings();

    Long getBatchId();
    Long getBatchCourseId();
    String getBatchName();

    Boolean getSeatLimited();
    Integer getMaxSeatLimit();

    LocalDate getStartDate();
    LocalTime getStartTime();

    BatchStatus getBatchStatus();
    BatchEnrollmentStatus getEnrollmentStatus();

    LocalDateTime getBatchCreatedDate();
    LocalDateTime getBatchLastModifiedDate();
}
