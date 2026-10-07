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

package edu.vinu.domain.post.services;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.post.dto.request.MyPostsFilterRequests;
import edu.vinu.domain.post.dto.response.PostResponse;
import edu.vinu.domain.post.dto.response.UserPostResponse;

public interface UserPostService {
    PaginatedApiResponse<UserPostResponse> getMyPosts(PaginationRequest pagination, MyPostsFilterRequests filters);
}
