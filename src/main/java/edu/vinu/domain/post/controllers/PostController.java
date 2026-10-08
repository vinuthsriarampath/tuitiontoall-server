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

package edu.vinu.domain.post.controllers;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.post.dto.request.MyPostsFilterRequests;
import edu.vinu.domain.post.dto.request.PostCreateRequest;
import edu.vinu.domain.post.dto.response.UserPostResponse;
import edu.vinu.domain.post.services.PostService;
import edu.vinu.domain.post.services.UserPostService;
import edu.vinu.domain.post_comment.dtos.request.PostCommentRequest;
import edu.vinu.domain.post_comment.service.PostCommentService;
import edu.vinu.domain.post_like.services.PostLikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v2/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserPostService userPostService;
    private final PostLikeService postLikeService;
    private final PostCommentService postCommentService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createPost(
            @RequestPart("request") PostCreateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        return ResponseEntity.ok(postService.createPost(request, files));
    }

    @PatchMapping("/{postId}/publish")
    public ResponseEntity<ApiResponse> publishDraftPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.publishDraftPost(postId));
    }

    @GetMapping("me")
    public ResponseEntity<PaginatedApiResponse<UserPostResponse>> getMyPosts(PaginationRequest paginationRequest, MyPostsFilterRequests filters){
        return ResponseEntity.ok(userPostService.getMyPosts(paginationRequest,filters));
    }

    @GetMapping("/user/{targetUserId}")
    public ResponseEntity<PaginatedApiResponse<UserPostResponse>> getUserPosts(@PathVariable Long targetUserId, PaginationRequest pagination) {
        return ResponseEntity.ok(userPostService.getUserPosts(targetUserId, pagination));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse> likePost(@PathVariable Long postId) {
        postLikeService.likePost(postId);
        return ResponseEntity.ok(new ApiResponse("Post liked successfully.",null));
    }

    @DeleteMapping("/{postId}/dislike")
    public ResponseEntity<ApiResponse> dislikePost(@PathVariable Long postId) {
        postLikeService.dislikePost(postId);
        return ResponseEntity.ok(new ApiResponse("Post disliked successfully.",null));
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<ApiResponse> commentPost(@PathVariable Long postId, @Valid @RequestBody PostCommentRequest request) {
        return ResponseEntity.ok(postCommentService.comment(postId, request));
    }
}
