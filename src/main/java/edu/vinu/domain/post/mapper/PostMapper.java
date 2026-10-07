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
import edu.vinu.domain.post.dto.response.UserPostResponse;
import edu.vinu.domain.post.dto.response.PostResponse;
import edu.vinu.domain.post.entity.Post;
import edu.vinu.domain.post.enums.PostStatus;
import edu.vinu.domain.post.enums.PostVisibility;
import edu.vinu.domain.post.repository.projections.UserPostProjection;
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

    public static PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .caption(post.getCaption())
                .visibility(post.getVisibility())
                .status(post.getStatus())
                .createdDate(post.getCreatedDate())
                .publishedDate(post.getPublishedDate())
                .lastModifiedDate(post.getUpdatedDate())
                .mediaList(post.getMediaList().stream().map(PostMediaMapper::toPostMediaResponse).toList())
                .build();
    }

    public static UserPostResponse toUserPostResponse(UserPostProjection p, List<PostMediaResponse> mediaList){
        return UserPostResponse.builder()
                .id(p.getId())
                .caption(p.getCaption())
                .visibility(p.getVisibility())
                .status(p.getStatus())
                .createdDate(p.getCreatedDate())
                .publishedDate(p.getPublishedDate())
                .lastModifiedDate(p.getUpdatedDate())
                .mediaList(mediaList)
                .likesCount(p.getLikesCount())
                .commentsCount(p.getCommentsCount())
                .isLiked(p.getIsLikedByUser() > 0)
                .build();
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
