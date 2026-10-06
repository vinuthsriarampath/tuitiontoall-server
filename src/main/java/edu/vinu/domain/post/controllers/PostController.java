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

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.post.dto.request.PostCreateRequest;
import edu.vinu.domain.post.dto.response.PostResponse;
import edu.vinu.domain.post.enums.PostMediaType;
import edu.vinu.domain.post.services.PostService;
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
}
