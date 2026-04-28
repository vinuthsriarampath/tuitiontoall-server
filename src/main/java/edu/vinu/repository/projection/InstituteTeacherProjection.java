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

import edu.vinu.enums.InstituteTeacherStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface InstituteTeacherProjection{
    Long getId();
    InstituteTeacherStatus getStatus();

    Long getInstituteId();

    Long getUserId();
    String getEmail();
    String getContact();
    String getDp();
    String getAddress();

    String getFirstName();
    String getLastName();
    LocalDate getDob();

    LocalDateTime getJoinedDate();
    LocalDateTime getLastModifiedDate();
}
