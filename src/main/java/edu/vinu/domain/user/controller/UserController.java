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

package edu.vinu.domain.user.controller;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.user.dto.User;
import edu.vinu.domain.user.dto.UserBasicResponse;
import edu.vinu.domain.user.request.UserBasicFilterRequest;
import edu.vinu.domain.user.service.UserService;
import edu.vinu.domain.user_follow.services.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v2/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserFollowService userFollowService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getUserDetails(){
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(new ApiResponse("User Verified!",user));
    }

    @DeleteMapping("/disable/me")
    public ResponseEntity<ApiResponse> disableMyAccount(){
        userService.disableUserAccountByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.status(OK).body(new ApiResponse("User disabled successfully!",null));
    }

    @GetMapping("/by-user-slug/{userSlug}")
    public ResponseEntity<ApiResponse> getUserByUserSlug(@PathVariable String userSlug){
        User user = userService.getUserByUserSlug(userSlug);
        return ResponseEntity.status(OK).body(new ApiResponse("User Found By "+userSlug,user));
    }

    @GetMapping("me/followers")
    public ResponseEntity<PaginatedApiResponse<UserBasicResponse>> getMyFollowers(PaginationRequest pagination, UserBasicFilterRequest filters){
        return ResponseEntity.ok(userFollowService.getMyFollowers(pagination,filters));
    }

    @GetMapping("me/followings")
    public ResponseEntity<PaginatedApiResponse<UserBasicResponse>> getMyFollowings(PaginationRequest pagination, UserBasicFilterRequest filters){
        return ResponseEntity.ok(userFollowService.getMyFollowings(pagination,filters));
    }

}
