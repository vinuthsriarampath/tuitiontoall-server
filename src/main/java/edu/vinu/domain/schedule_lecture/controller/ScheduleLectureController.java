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

package edu.vinu.domain.schedule_lecture.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.schedule_lecture.request.create.ScheduleLectureCreateRequest;
import edu.vinu.domain.schedule_lecture.request.update.ScheduleLectureUpdateRequest;
import edu.vinu.domain.schedule_lecture.response.ScheduleLectureResponse;
import edu.vinu.domain.schedule_lecture.service.ScheduleLectureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequestMapping("api/v2/schedule-lectures")
@RequiredArgsConstructor
public class ScheduleLectureController {
    private final ScheduleLectureService scheduleLectureService;

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleLectureResponse>> scheduleLecture(@Valid @RequestBody ScheduleLectureCreateRequest request) {
        return ResponseEntity.ok(createApiResponse("Lecture Scheduled Successfully!", scheduleLectureService.scheduleLecture(request)));
    }

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ScheduleLectureResponse>> updateScheduleLecture(@PathVariable Long id, @Valid @RequestBody ScheduleLectureUpdateRequest request) {
        return ResponseEntity.ok(createApiResponse("Lecture Updated Successfully!", scheduleLectureService.updateScheduleLecture(id, request)));
    }
}
