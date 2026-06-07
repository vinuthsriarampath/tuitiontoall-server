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

package edu.vinu.domain.assignment.controller;

import edu.vinu.domain.assignment.request.chapter_assignments.ChapterAssignmentCreateRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.assignment.response.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.domain.assignment.service.ChapterAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/chapter-assignments")
@RequiredArgsConstructor
public class ChapterAssignmentController {
    private final ChapterAssignmentService chapterAssignmentService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> createChapterAssignment(@RequestPart("file")MultipartFile file, @Valid @RequestPart("request")ChapterAssignmentCreateRequest request){
        ChapterAssignmentResponse response = chapterAssignmentService.createChapterAssignment(request,file);
        return ResponseEntity.ok(new ApiResponse("Chapter assignment created successfully", response));
    }
}
