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

package edu.vinu.request.announcements;

import edu.vinu.enums.AnnouncementVisibility;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementVisibilityUpdateRequest {
    @NotNull(message = "Announcement visibility must not be null")
    private AnnouncementVisibility visibility;
    @Positive(message = "courseId must be a positive number")
    private Long courseId;
    @Positive(message = "batchId must be a positive number")
    private Long batchId;
}
