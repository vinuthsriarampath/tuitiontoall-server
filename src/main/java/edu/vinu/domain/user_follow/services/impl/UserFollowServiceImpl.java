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

package edu.vinu.domain.user_follow.services.impl;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.exception.custom.BadRequestException;
import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.user.dto.UserBasicResponse;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.mapper.UserMapper;
import edu.vinu.domain.user.request.UserBasicFilterRequest;
import edu.vinu.domain.user.service.UserService;
import edu.vinu.domain.user_follow.dtos.response.UserUnfollowResponse;
import edu.vinu.domain.user_follow.entity.UserFollow;
import edu.vinu.domain.user_follow.mapper.UserFollowMapper;
import edu.vinu.domain.user_follow.repository.UserFollowRepository;
import edu.vinu.domain.user_follow.services.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFollowServiceImpl implements UserFollowService {

    private final UserAuthenticationService authService;
    private final UserService userService;
    private final UserFollowRepository userFollowRepository;

    @Override
    public ApiResponse followUser(Long followingId) {

        UserEntity followerUser = userService.getUserEntityByEmail(authService.getCurrentUserEmail());
        UserEntity followingUser = userService.getUserEntityById(followingId);

        if(followerUser.getId().equals(followingUser.getId())) {
            throw new BadRequestException("You cannot follow yourself");
        }

        if(userFollowRepository.existsByFollowerIdAndFollowingId(followerUser.getId(), followingUser.getId()) > 0) {
            throw new BadRequestException("You are already following this user");
        }

        UserFollow userFollow = UserFollow.builder()
                .follower(followerUser)
                .followingUser(followingUser)
                .build();

        UserFollow saved = userFollowRepository.save(userFollow);

        return ApiResponse.builder()
                .message("User followed successfully")
                .data(UserFollowMapper.toResponse(saved))
                .build();
    }

    @Override
    public ApiResponse unfollowUser(Long followingId) {

        UserEntity followerUser = userService.getUserEntityByEmail(authService.getCurrentUserEmail());
        UserEntity followingUser = userService.getUserEntityById(followingId);

        UserFollow userFollow = userFollowRepository.getByFollowerAndFollowingId(followerUser.getId(), followingUser.getId())
                .orElseThrow(() -> new NotFoundException("You are not following the user!"));

        userFollowRepository.delete(userFollow);

        UserUnfollowResponse response = UserUnfollowResponse.builder()
                .id(userFollow.getId())
                .followingId(userFollow.getFollowingUser().getId())
                .build();

        return ApiResponse.builder()
                .message("User Unfollowed Successfully!")
                .data(response)
                .build();
    }

    @Override
    public PaginatedApiResponse<UserBasicResponse> getMyFollowers(PaginationRequest pagination, UserBasicFilterRequest filters) {
        UserEntity userEntity = userService.getUserEntityByEmail(authService.getCurrentUserEmail());

        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of("followed_on")));

        Page<UserBasicResponse> page =userFollowRepository.getFollowersByFollowingId(
                userEntity.getId(),
                filters.id(),
                filters.displayName(),
                filters.email(),
                filters.userSlug(),
                filters.role(),
                pageable
        ).map(UserMapper::toUserBasicResponse);

        return PaginatedApiResponse.<UserBasicResponse>builder()
                .message("My Followers")
                .data(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public PaginatedApiResponse<UserBasicResponse> getMyFollowings(PaginationRequest pagination, UserBasicFilterRequest filters) {
        UserEntity userEntity = userService.getUserEntityByEmail(authService.getCurrentUserEmail());

        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(), pagination.sortBy(), List.of("followed_on")));

        Page<UserBasicResponse> page =userFollowRepository.getFollowingsByFollowerId(
                userEntity.getId(),
                filters.id(),
                filters.displayName(),
                filters.email(),
                filters.userSlug(),
                filters.role(),
                pageable
        ).map(UserMapper::toUserBasicResponse);

        return PaginatedApiResponse.<UserBasicResponse>builder()
                .message("My Followings")
                .data(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
