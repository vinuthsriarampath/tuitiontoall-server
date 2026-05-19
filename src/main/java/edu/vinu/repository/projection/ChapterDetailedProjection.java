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

import edu.vinu.enums.ChapterStatus;
import edu.vinu.enums.ModuleStatus;

import java.time.LocalDateTime;

public interface ChapterDetailedProjection {
    Long getId();
    String getTitle();
    int getChapterOrder();
    ChapterStatus getStatus();
    LocalDateTime getCreatedDate();
    LocalDateTime getLastModifiedDate();

    Long getModuleId();
    String getModuleName();
    ModuleStatus getModuleStatus();
    Long getModuleBatchId();
    Long getModuleTeacherId();
    LocalDateTime getModuleCreatedDate();
    LocalDateTime getModuleLastModifiedDate();
}
