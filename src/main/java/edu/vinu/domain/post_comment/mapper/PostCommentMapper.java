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

package edu.vinu.domain.post_comment.mapper;

import edu.vinu.domain.post_comment.dtos.response.PostCommentResponse;
import edu.vinu.domain.post_comment.entity.PostComment;

public class PostCommentMapper{
    public static PostCommentResponse toPostCommentResponse(PostComment comment) {
        return PostCommentResponse.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                .createdDate(comment.getCreatedDate())
                .build();
    }
}
