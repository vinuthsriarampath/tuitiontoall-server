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

package edu.vinu.domain.student_assignment_submit.repository.projections;

import java.time.LocalDateTime;

public interface RecentResultsProjection {
    Long getAssignmentId();
    String getAssignmentTitle();
    String getGrade();
    Integer getMarksGained();
    LocalDateTime getSubmittedAt();
}
