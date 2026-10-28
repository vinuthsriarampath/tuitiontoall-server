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

package edu.vinu.domain.lecture_record.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.lecture_record.request.LectureRecordDetailsUpdateRequest;
import edu.vinu.domain.lecture_record.request.LectureRecordUploadInitRequest;
import edu.vinu.domain.lecture_record.response.LectureRecordChunkUploadResponse;
import edu.vinu.domain.lecture_record.response.LectureRecordResponse;
import edu.vinu.domain.lecture_record.response.LectureRecordUploadInitResponse;
import edu.vinu.domain.lecture_record.service.LectureRecordService;
import edu.vinu.domain.lecture_record.service.VideoStreamTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/lecture-records")
public class LectureRecordController {
    private final LectureRecordService lectureRecordService;
    private final VideoStreamTokenService videoStreamTokenService;

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PostMapping("/upload/init")
    public ResponseEntity<ApiResponse<LectureRecordUploadInitResponse>> initializeUpload(@Valid @RequestBody LectureRecordUploadInitRequest request) {
        return ResponseEntity.ok(createApiResponse("Upload initialized successfully", lectureRecordService.initializeUpload(request)));
    }

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PostMapping("/upload/chunk")
    public ResponseEntity<ApiResponse<LectureRecordChunkUploadResponse>> uploadChunk(
            @RequestParam String uploadId,
            @RequestParam Integer chunkIndex,
            @RequestParam MultipartFile chunk
    ) {
        return ResponseEntity.ok(createApiResponse("Upload chunk successfully", lectureRecordService.uploadChunk(uploadId, chunkIndex, chunk)));
    }

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PostMapping("/upload/complete/{uploadId}")
    public ResponseEntity<ApiResponse<LectureRecordResponse>> completeUpload(@PathVariable String uploadId) {
        return ResponseEntity.ok(createApiResponse("Upload completed successfully", lectureRecordService.completeUpload(uploadId)));
    }

    @GetMapping("/stream/{fileName:.+}")
    public ResponseEntity<ResourceRegion> streamVideo(@PathVariable String fileName, @RequestParam String token, @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        videoStreamTokenService.validateToken(token);
        return lectureRecordService.streamVideo(fileName, rangeHeader);
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("/stream-token/{fileName:.+}")
    public ResponseEntity<ApiResponse<String>> generateStreamToken(@PathVariable String fileName) {
        return ResponseEntity.ok(createApiResponse("Stream token generated for file name: " + fileName, videoStreamTokenService.generateToken(fileName)));
    }

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PutMapping("{id}/details")
    public ResponseEntity<ApiResponse<LectureRecordResponse>> updateLectureRecordDetails(@PathVariable Long id, @Valid @RequestBody LectureRecordDetailsUpdateRequest request) {
        return ResponseEntity.ok(createApiResponse("Lecture record details updated successfully", lectureRecordService.updateLectureRecordDetails(id, request)));
    }
}
