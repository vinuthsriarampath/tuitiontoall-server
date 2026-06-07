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

package edu.vinu.domain.module.repository.projection;

import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.module.enums.ModuleStatus;

import java.time.LocalDateTime;

public interface DetailedModuleProjection {
    Long getId();
    String getName();
    ModuleStatus getStatus();
    LocalDateTime getCreatedDate();
    LocalDateTime getLastModifiedDate();

    Long getBatchId();
    Long getCourseId();
    String getBatchName();
    BatchStatus getBatchStatus();
    BatchEnrollmentStatus getBatchEnrollmentStatus();
    LocalDateTime getBatchCreatedDate();
    LocalDateTime getBatchLastModifiedDate();

    Long getTeacherId();
    String getTeacherFirstName();
    String getTeacherLastName();

    String getUserEmail();
    String getUserContact();
    String getUserDp();
    String getUserSlug();
}
