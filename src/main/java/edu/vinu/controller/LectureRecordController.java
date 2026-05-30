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

import edu.vinu.request.lecture_record.LectureRecordDetailsUpdateRequest;
import edu.vinu.request.lecture_record.LectureRecordUploadInitRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.lecture_record.LectureRecordChunkUploadResponse;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import edu.vinu.response.lecture_record.LectureRecordUploadInitResponse;
import edu.vinu.service.common.LectureRecordService;
import edu.vinu.service.common.VideoStreamTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/lecture-records")
public class LectureRecordController {
    private final LectureRecordService lectureRecordService;
    private final VideoStreamTokenService videoStreamTokenService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/upload/init")
    public ResponseEntity<ApiResponse> initializeUpload(@Valid @RequestBody LectureRecordUploadInitRequest request){
        LectureRecordUploadInitResponse response = lectureRecordService.initializeUpload(request);

        return ResponseEntity.ok(new ApiResponse("Upload initialized successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/upload/chunk")
    public ResponseEntity<ApiResponse> uploadChunk(
            @RequestParam String uploadId,
            @RequestParam Integer chunkIndex,
            @RequestParam MultipartFile chunk
    ){
        LectureRecordChunkUploadResponse response = lectureRecordService.uploadChunk(uploadId, chunkIndex, chunk);
        return ResponseEntity.ok(new ApiResponse("Upload chunk successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/upload/complete/{uploadId}")
    public ResponseEntity<ApiResponse> completeUpload(@PathVariable String uploadId) {

        LectureRecordResponse response = lectureRecordService.completeUpload(uploadId);
        return ResponseEntity.ok(new ApiResponse("Upload completed successfully", response));
    }

    @GetMapping("/stream/{fileName:.+}")
    public ResponseEntity<ResourceRegion> streamVideo(@PathVariable String fileName, @RequestParam String token, @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        videoStreamTokenService.validateToken(token);
        return lectureRecordService.streamVideo( fileName, rangeHeader );
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/stream-token/{fileName:.+}")
    public ResponseEntity<ApiResponse> generateStreamToken(@PathVariable String fileName){
        String token = videoStreamTokenService.generateToken(fileName);
        return ResponseEntity.ok(new ApiResponse("Stream token generated for file name: "+fileName, token));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PutMapping("{id}/details")
    public ResponseEntity<ApiResponse> updateLectureRecordDetails(@PathVariable Long id, @Valid @RequestBody LectureRecordDetailsUpdateRequest request){
        LectureRecordResponse response = lectureRecordService.updateLectureRecordDetails(id,request);
        return ResponseEntity.ok(new ApiResponse("Lecture record details updated successfully", response));
    }
}
