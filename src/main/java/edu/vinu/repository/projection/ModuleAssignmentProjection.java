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

package edu.vinu.repository.projection;

import java.time.LocalDateTime;

public interface ModuleAssignmentProjection {
    Long getId();
    Long getModuleId();
    Long getAssignmentId();
    String getTopic();
    String getDescription();
    String getFileName();
    Integer getTotalMarks();
    LocalDateTime getAvailableOn();
    LocalDateTime getDueDate();
    boolean getLateSubmission();
    boolean getResubmission();
    Integer getMaxAttempts();
    LocalDateTime getCreatedDate();
    LocalDateTime getLastModifiedDate();
}
