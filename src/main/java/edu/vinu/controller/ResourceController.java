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

package edu.vinu.controller;

import edu.vinu.request.resource.ResourceInitRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.resource.ResourceChunkUploadResponse;
import edu.vinu.response.resource.ResourceInitResponse;
import edu.vinu.service.common.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v2/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/upload/init")
    public ResponseEntity<ApiResponse> initializeUpload(@Valid @RequestBody ResourceInitRequest request) {
        ResourceInitResponse response = resourceService.initializeUpload(request);
        return ResponseEntity.ok(new ApiResponse("Upload initialized successfully", response));
    }
    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/upload/chunk")
    public ResponseEntity<ApiResponse> uploadChunk(
            @RequestParam String uploadId,
            @RequestParam Integer chunkIndex,
            @RequestParam MultipartFile file
    ){
        ResourceChunkUploadResponse response = resourceService.uploadChunk(uploadId,chunkIndex, file);
        return ResponseEntity.ok(new ApiResponse("Chunk uploaded successfully", response));
    }
}
