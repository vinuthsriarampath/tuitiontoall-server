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

package edu.vinu.domain.user_follow.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.user_follow.services.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/unfollows")
@RequiredArgsConstructor
public class UserUnfollowController {

    private final UserFollowService userFollowService;

    @DeleteMapping("/user/{followingId}")
    public ResponseEntity<ApiResponse> unfollowUser(@PathVariable Long followingId){
        return ResponseEntity.ok(userFollowService.unfollowUser(followingId));
    }
}
