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

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.post.entity.Post;
import edu.vinu.domain.post.services.PostService;
import edu.vinu.domain.post_comment.dtos.request.PostCommentRequest;
import edu.vinu.domain.post_comment.dtos.request.PostCommentsFilterRequest;
import edu.vinu.domain.post_comment.dtos.response.PostCommentDetailedResponse;
import edu.vinu.domain.post_comment.entity.PostComment;
import edu.vinu.domain.post_comment.mapper.PostCommentMapper;
import edu.vinu.domain.post_comment.repository.PostCommentRepository;
import edu.vinu.domain.post_comment.service.PostCommentService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PaginatedApiResponse<PostCommentDetailedResponse> getPostComments(Long postId, PaginationRequest pagination, PostCommentsFilterRequest filters) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of("created_date")));

        Page<PostCommentDetailedResponse> page = commentRepository.findAllByPostId(
                postId,
                filters.userId(),
                filters.userName(),
                pageable
        ).map(PostCommentMapper::toPostCommentDetailedResponse);

        return PaginatedApiResponse.<PostCommentDetailedResponse>builder()
                .message("Comments retrieved successfully")
                .data(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
