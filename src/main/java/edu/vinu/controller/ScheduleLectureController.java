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

import edu.vinu.request.schedule_lecture.ScheduleLectureCreateRequest;
import edu.vinu.request.schedule_lecture.ScheduleLectureUpdateRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.response.schedule_lecture.ScheduleLectureResponse;
import edu.vinu.service.common.ScheduleLectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/schedule-lectures")
@RequiredArgsConstructor
public class ScheduleLectureController {
    private final ScheduleLectureService scheduleLectureService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping
    public ResponseEntity<ApiResponse> scheduleLecture(@Valid @RequestBody ScheduleLectureCreateRequest request){
        ScheduleLectureResponse response = scheduleLectureService.scheduleLecture(request);
        return ResponseEntity.ok(new ApiResponse("Lecture Scheduled Successfully!",response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateScheduleLecture(@PathVariable Long id, @Valid @RequestBody ScheduleLectureUpdateRequest request) {
        ScheduleLectureResponse response = scheduleLectureService.updateScheduleLecture(id , request);
        return ResponseEntity.ok(new ApiResponse("Lecture Updated Successfully!", response));
    }
}
