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

package edu.vinu.domain.post.mapper;

import edu.vinu.domain.post.dto.request.PostCreateRequest;
import edu.vinu.domain.post.dto.request.enums.PostCreateStatus;
import edu.vinu.domain.post.dto.response.PostMediaResponse;
import edu.vinu.domain.post.dto.response.PostResponse;
import edu.vinu.domain.post.entity.Post;
import edu.vinu.domain.post.entity.PostMedia;
import edu.vinu.domain.post.enums.PostStatus;
import edu.vinu.domain.post.enums.PostVisibility;
import edu.vinu.domain.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public class PostMapper {
    public static Post toPostEntity(PostCreateRequest request, UserEntity user) {
        PostStatus status = request.status() != null ? toPostStatus(request.status()) : PostStatus.PUBLISHED;

        return Post.builder()
                .user(user)
                .caption(request.caption())
                .visibility(request.visibility() != null ? request.visibility() : PostVisibility.PUBLIC)
                .status(status)
                .publishedDate(status == PostStatus.PUBLISHED ? LocalDateTime.now() : null)
                .build();
    }

    public static PostMediaResponse toPostMediaResponse(PostMedia media) {
        return new PostMediaResponse(
                media.getId(),
                media.getMediaUrl(),
                media.getMediaType(),
                media.getFileOrder()
        );
    }

    public static PostResponse toPostResponse(Post post) {
        List<PostMediaResponse> mediaResponses = post.getMediaList() != null ?
                post.getMediaList().stream().map(PostMapper::toPostMediaResponse).toList()
                : List.of();

        return new PostResponse(
                post.getId(),
                post.getUser().getId(),
                post.getCaption(),
                post.getVisibility(),
                post.getStatus(),
                post.getCreatedDate(),
                post.getPublishedDate(),
                post.getUpdatedDate(),
                mediaResponses
        );
    }

    private static PostStatus toPostStatus(PostCreateStatus createStatus) {
        if (createStatus == null) {
            return PostStatus.DRAFT;
        }
        return switch (createStatus) {
            case DRAFT -> PostStatus.DRAFT;
            case PUBLISHED -> PostStatus.PUBLISHED;
        };
    }
}
