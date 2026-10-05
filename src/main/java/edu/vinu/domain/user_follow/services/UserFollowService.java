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

package edu.vinu.domain.user_follow.services;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.user.dto.UserBasicResponse;
import edu.vinu.domain.user.request.UserBasicFilterRequest;

public interface UserFollowService {
    ApiResponse followUser(Long followingId);

    ApiResponse unfollowUser(Long followingId);

    PaginatedApiResponse<UserBasicResponse> getMyFollowers(PaginationRequest pagination, UserBasicFilterRequest filters);

    PaginatedApiResponse<UserBasicResponse> getMyFollowings(PaginationRequest pagination, UserBasicFilterRequest filters);
}
