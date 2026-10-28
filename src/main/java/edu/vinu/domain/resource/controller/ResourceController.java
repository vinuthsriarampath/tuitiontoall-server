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

package edu.vinu.domain.resource.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.resource.request.ResourceInitRequest;
import edu.vinu.domain.resource.response.ResourceChunkUploadResponse;
import edu.vinu.domain.resource.response.ResourceInitResponse;
import edu.vinu.domain.resource.response.ResourceResponse;
import edu.vinu.domain.resource.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequestMapping("api/v2/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PostMapping("/upload/init")
    public ResponseEntity<ApiResponse<ResourceInitResponse>> initializeUpload(@Valid @RequestBody ResourceInitRequest request) {
        return ResponseEntity.ok(createApiResponse("Upload initialized successfully", resourceService.initializeUpload(request)));
    }
    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PostMapping("/upload/chunk")
    public ResponseEntity<ApiResponse<ResourceChunkUploadResponse>> uploadChunk(
            @RequestParam String uploadId,
            @RequestParam Integer chunkIndex,
            @RequestParam MultipartFile file
    ){
        return ResponseEntity.ok(createApiResponse("Chunk uploaded successfully", resourceService.uploadChunk(uploadId,chunkIndex, file)));
    }

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PostMapping("/upload/complete/{uploadId}")
    public ResponseEntity<ApiResponse<ResourceResponse>> completeUpload(@PathVariable("uploadId") String uploadId){
        return ResponseEntity.ok(createApiResponse("Upload completed successfully",resourceService.completeUpload(uploadId)));
    }

    @GetMapping("/view/{fileName:.+}")
    public ResponseEntity<ResourceRegion> viewResource(@PathVariable String fileName, @RequestHeader(value = "Range", required = false) String range){
        return resourceService.viewResource(fileName,range);
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadResource(@PathVariable String fileName){
        return resourceService.downloadFile(fileName);
    }

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @DeleteMapping("/{resourceId}")
    public ResponseEntity<ApiResponse<Void>> deleteResource(@PathVariable Long resourceId){
        resourceService.deleteResource(resourceId);
        return ResponseEntity.ok(createApiResponse("Resource deleted successfully", null));
    }

}
