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

package edu.vinu.domain.post_comment.service.impl;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.post.entity.Post;
import edu.vinu.domain.post.services.PostService;
import edu.vinu.domain.post_comment.dtos.request.PostCommentRequest;
import edu.vinu.domain.post_comment.entity.PostComment;
import edu.vinu.domain.post_comment.mapper.PostCommentMapper;
import edu.vinu.domain.post_comment.repository.PostCommentRepository;
import edu.vinu.domain.post_comment.service.PostCommentService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {
    private final PostService postService;
    private final PostCommentRepository commentRepository;
    private final UserService userService;

    @Override
    public ApiResponse comment(Long postId, PostCommentRequest request) {
        Post post = postService.getPostById(postId);
        UserEntity currentUser = userService.getCurrentUser();

        PostComment comment = PostComment.builder()
                .post(post)
                .user(currentUser)
                .message(request.message())
                .build();

        PostComment save = commentRepository.save(comment);

        return ApiResponse.builder()
                .message("Comment added successfully")
                .data(PostCommentMapper.toPostCommentResponse(save))
                .build();
    }
}
