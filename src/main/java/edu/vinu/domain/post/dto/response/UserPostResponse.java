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

package edu.vinu.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.vinu.domain.post.enums.PostStatus;
import edu.vinu.domain.post.enums.PostVisibility;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record UserPostResponse(
        Long id,
        String caption,
        PostVisibility visibility,
        PostStatus status,
        LocalDateTime createdDate,
        LocalDateTime publishedDate,
        LocalDateTime lastModifiedDate,

        List<PostMediaResponse>mediaList,

        long likesCount,
        long commentsCount,

        @JsonProperty("isLiked")
        boolean isLiked
) {
}
