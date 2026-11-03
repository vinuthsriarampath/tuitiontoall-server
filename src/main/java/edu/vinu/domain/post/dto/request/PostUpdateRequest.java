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

package edu.vinu.domain.post.dto.request;

import edu.vinu.domain.post.enums.PostStatus;
import edu.vinu.domain.post.enums.PostVisibility;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostUpdateRequest(
        String caption,
        @NotNull(message = "visibility cannot be null")
        PostVisibility visibility,
        @NotNull(message = "status cannot be null")
        PostStatus status,
        List<Long> removeMediaIds,
        List<PostMediaItemRequest> newMediaItems
) {
}
