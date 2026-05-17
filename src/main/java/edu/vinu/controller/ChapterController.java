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

import edu.vinu.request.chapter.ChapterCreateRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.chapter.ChapterResponse;
import edu.vinu.service.common.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping
    public ResponseEntity<ApiResponse> createChapter(@Valid @RequestBody ChapterCreateRequest request){
        ChapterResponse response = chapterService.createChapter(request);
        return ResponseEntity.ok(new ApiResponse("Chapter created successfully", response));
    }
}
