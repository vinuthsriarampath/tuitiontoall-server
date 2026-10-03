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
import edu.vinu.domain.module.enums.ModuleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TeacherCourseProjection {

    Long getCourseId();
    String getCourseTitle();
    String getCourseDescription();
    CourseCategory getCourseCategory();
    CourseStatus getCourseStatus();
    CourseLanguage getCourseLanguage();
    CourseLevel getCourseLevel();
    CourseMode getCourseMode();
    String getCourseThumbnail();
    Integer getCourseDurationInHours();
    BigDecimal getCourseAvgRating();
    Integer getCourseTotalRatings();

    Long getBatchId();
    String getBatchName();
    BatchStatus getBatchStatus();
    BatchEnrollmentStatus getBatchEnrollmentStatus();
    LocalDate getBatchStartDate();
    LocalTime getBatchStartTime();
    LocalDateTime getBatchCreatedDate();
    LocalDateTime getBatchLastModifiedDate();

    Long getModuleId();
    String getModuleName();
    ModuleStatus getModuleStatus();
    LocalDateTime getModuleCreatedDate();
    LocalDateTime getModuleLastModifiedDate();
}
