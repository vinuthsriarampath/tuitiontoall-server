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

package edu.vinu.domain.announcement.request.create;

import edu.vinu.domain.announcement.enums.AnnouncementVisibility;
import edu.vinu.domain.announcement.request.create.enums.AnnouncementCreateStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementCreateRequest {
    @NotBlank(message = "Title is Mandatory")
    private String title;
    @NotBlank(message = "Description is Mandatory")
    private String description;
    @NotNull(message = "Visibility is Mandatory")
    private AnnouncementVisibility visibility;
    @NotNull(message = "Visibility is Mandatory")
    private AnnouncementCreateStatus status;
    @Future(message = "ExpireAt must be a future date/time")
    @NotNull(message = "ExpireAt is Mandatory")
    private LocalDateTime expireAt;
    @Positive(message = "courseId must be a positive number")
    private Long courseId;
    @Positive(message = "batchId must be a positive number")
    private Long batchId;
}
