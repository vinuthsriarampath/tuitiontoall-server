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

package edu.vinu.domain.application.response;

import edu.vinu.domain.user.response.TeacherUserResponse;
import edu.vinu.domain.application.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDetailsResponse {
    private Long id;
    private Long teacherVacancyId;
    private ApplicationStatus status;
    private LocalDateTime appliedDate;
    private LocalDateTime lastModifiedDate;

    private TeacherUserResponse teacher;
}
