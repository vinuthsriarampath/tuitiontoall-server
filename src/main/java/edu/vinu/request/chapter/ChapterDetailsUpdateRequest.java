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

package edu.vinu.request.chapter;

import edu.vinu.domain.chapter.enums.ChapterStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ChapterDetailsUpdateRequest(
        @NotBlank(message = "Title is mandatory!")
        String title,
        @NotNull(message = "Chapter status is mandatory!")
        ChapterStatus status,
        @NotNull(message = "Module id is mandatory!")
        Long moduleId
) {
}
