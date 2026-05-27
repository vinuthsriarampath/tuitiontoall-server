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

package edu.vinu.request.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceInitRequest(
        @NotBlank(message = "Name is mandatory!")
        String name,
        @NotNull(message = "Chapter ID is mandatory!")
        Long chapterId,
        @NotBlank(message = "Original file name is mandatory!")
        String originalFileName,
        @NotNull(message = "Total file size is mandatory!")
        Long totalSize,
        @NotNull(message = "Total chunks is mandatory!")
        Integer totalChunks
) {
}
