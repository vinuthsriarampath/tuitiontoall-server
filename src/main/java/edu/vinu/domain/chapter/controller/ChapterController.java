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

package edu.vinu.domain.chapter.controller;

import edu.vinu.request.assignments.chapter_assignments.ChapterAssignmentFilterRequest;
import edu.vinu.domain.chapter.request.ChapterCreateRequest;
import edu.vinu.domain.chapter.request.ChapterDetailsUpdateRequest;
import edu.vinu.domain.chapter.request.ChapterReorderRequest;
import edu.vinu.request.resource.ResourceFilterRequest;
import edu.vinu.request.schedule_lecture.ScheduleLectureFilterRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.response.assignments.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.domain.chapter.response.ChapterDetailedResponse;
import edu.vinu.domain.chapter.response.ChapterResponse;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import edu.vinu.response.resource.ResourceResponse;
import edu.vinu.response.schedule_lecture.ScheduleLectureResponse;
import edu.vinu.domain.chapter.service.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/details")
    public ResponseEntity<ApiResponse> updateChapterDetails(@PathVariable("id")Long id,@Valid @RequestBody ChapterDetailsUpdateRequest request){
        ChapterResponse response = chapterService.updateChapterDetailsById(id,request);
        return ResponseEntity.ok(new ApiResponse("Chapter details updated successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("reorder")
    public ResponseEntity<ApiResponse> reorderChapters(@Valid @RequestBody ChapterReorderRequest request) {
        List<ChapterResponse> responses = chapterService.reorderChapters(request);

        return ResponseEntity.ok(
                new ApiResponse(
                        "Chapters reordered successfully!",
                        responses
                )
        );
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("{id}/detailed")
    public ResponseEntity<ApiResponse> getDetailedChapterById(@PathVariable("id")Long id){
        ChapterDetailedResponse response = chapterService.getDetailedChapterById(id);
        return ResponseEntity.ok(new ApiResponse("Chapter details fetched successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("{id}/lecture-records")
    public ResponseEntity<ApiResponse> getAllLectureRecordsById(@PathVariable("id")Long id){
        List<LectureRecordResponse> responses = chapterService.getAllLectureRecordsByChapterId(id);
        return ResponseEntity.ok(new ApiResponse("All lecture records related to chapter fetched successfully!", responses));
    }


    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("{id}/schedule-lectures")
    public ResponseEntity<PaginatedApiResponse<ScheduleLectureResponse>> getAllScheduleLecturesWithFilters(
            @PathVariable("id") Long chapterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "created_date")List<String> sortBy,
            ScheduleLectureFilterRequest filters
    ) {
        PaginatedApiResponse<ScheduleLectureResponse> response = chapterService.getAllScheduleLecturesByChapter(chapterId, page, size, direction, sortBy, filters);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("{id}/resources")
    public ResponseEntity<PaginatedApiResponse<ResourceResponse>> getAllResourcesWithFilters(
            @PathVariable("id") Long chapterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "created_date")List<String> sortBy,
            ResourceFilterRequest filters
    ){
        PaginatedApiResponse<ResourceResponse> response = chapterService.getAllResourcesByChapter(chapterId,page,size,direction,sortBy,filters);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("{id}/assignments")
    public ResponseEntity<PaginatedApiResponse<ChapterAssignmentResponse>> getAllChapterAssignmentsWithFilters(
            @PathVariable("id") Long chapterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "created_date")List<String> sortBy,
            ChapterAssignmentFilterRequest filters
    ){
        PaginatedApiResponse<ChapterAssignmentResponse> response = chapterService.getAllChapterAssignmentsByChapter(chapterId, page, size, direction, sortBy, filters);
        return ResponseEntity.ok(response);
    }
}
