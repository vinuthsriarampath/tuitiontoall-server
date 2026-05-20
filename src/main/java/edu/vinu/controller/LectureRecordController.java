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

import edu.vinu.request.lecture_record.LectureRecordUploadInitRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.lecture_record.LectureRecordUploadInitResponse;
import edu.vinu.service.common.LectureRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/lecture-records")
public class LectureRecordController {
    private final LectureRecordService lectureRecordService;

    @PostMapping("/upload/init")
    public ResponseEntity<ApiResponse> initializeUpload(@RequestBody LectureRecordUploadInitRequest request){
        LectureRecordUploadInitResponse response = lectureRecordService.initializeUpload(request);

        return ResponseEntity.ok(new ApiResponse("Upload initialized successfully", response));
    }
}
