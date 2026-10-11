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

package edu.vinu.domain.post.services.impl;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.post.dto.request.MyPostsFilterRequests;
import edu.vinu.domain.post.dto.response.FeedPostResponse;
import edu.vinu.domain.post.dto.response.UserPostResponse;
import edu.vinu.domain.post.mapper.PostMapper;
import edu.vinu.domain.post.repository.PostRepository;
import edu.vinu.domain.post.services.PostMediaService;
import edu.vinu.domain.post.services.UserPostService;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import edu.vinu.domain.user_follow.repository.UserFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPostServiceImpl implements UserPostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final PostMediaService postMediaService;
    private final UserFollowRepository userFollowRepository;

    @Transactional(readOnly = true)
    @Override
    public PaginatedApiResponse<UserPostResponse> getMyPosts(PaginationRequest pagination, MyPostsFilterRequests filters) {
        UserEntity currentUser = userService.getCurrentUser();

        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(),SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of("published_date")));

        Page<UserPostResponse> postsPage = postRepository.findAllByOwner(
                currentUser.getId(),
                filters.status() != null ? filters.status().name() : null,
                filters.visibility() != null ? filters.visibility().name() : null,
                pageable
        ).map(post -> PostMapper.toUserPostResponse(post,postMediaService.getPostMediaByPostId(post.getId())));

        return PaginatedApiResponse.<UserPostResponse>builder()
                .message("Fetched my posts successfully")
                .data(postsPage.getContent())
                .page(postsPage.getNumber())
                .size(postsPage.getSize())
                .totalElements(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
                .last(postsPage.isLast())
                .build();
    }

    @Override
    public PaginatedApiResponse<UserPostResponse> getUserPosts(Long targetUserId, PaginationRequest pagination) {
        UserEntity currentUser = userService.getCurrentUser();

        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of("published_date", "created_date")));

        Page<UserPostResponse> postsPage = Page.empty(pageable);
        if (currentUser != null && !currentUser.getId().equals(targetUserId)) {
            boolean isFollowing = userFollowRepository.isFollowing(targetUserId, currentUser.getId()) > 0;
            postsPage = postRepository.findTargetUserPosts(targetUserId,currentUser.getId(), isFollowing, pageable).map(post -> PostMapper.toUserPostResponse(post,postMediaService.getPostMediaByPostId(post.getId())));
        } else if (currentUser != null) {
            return getMyPosts(pagination, new MyPostsFilterRequests(null, null));
        }

        return PaginatedApiResponse.<UserPostResponse>builder()
                .message("Fetched User's posts successfully")
                .data(postsPage.getContent())
                .page(postsPage.getNumber())
                .size(postsPage.getSize())
                .totalElements(postsPage.getTotalElements())
                .totalPages(postsPage.getTotalPages())
                .last(postsPage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedApiResponse<FeedPostResponse> getFeedPosts(PaginationRequest pagination) {
        UserEntity currentUser = userService.getCurrentUser();

        Pageable pageable = PageRequest.of(
                pagination.page(),
                pagination.size(),
                SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of("published_date", "created_date"))
        );

        Page<FeedPostResponse> feedPage = postRepository.findFeedPosts(currentUser.getId(), pageable)
                .map(p -> PostMapper.toFeedPostResponse(p, postMediaService.getPostMediaByPostId(p.getId())));

        return PaginatedApiResponse.<FeedPostResponse>builder()
                .message("Feed posts fetched successfully")
                .data(feedPage.getContent())
                .page(feedPage.getNumber())
                .size(feedPage.getSize())
                .totalElements(feedPage.getTotalElements())
                .totalPages(feedPage.getTotalPages())
                .last(feedPage.isLast())
                .build();
    }
}
