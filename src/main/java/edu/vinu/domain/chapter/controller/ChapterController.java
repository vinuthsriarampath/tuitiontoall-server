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

import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.assignment.request.chapter_assignments.ChapterAssignmentFilterRequest;
import edu.vinu.domain.assignment.response.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.domain.chapter.request.ChapterCreateRequest;
import edu.vinu.domain.chapter.request.ChapterDetailsUpdateRequest;
import edu.vinu.domain.chapter.request.ChapterReorderRequest;
import edu.vinu.domain.chapter.response.ChapterDetailedResponse;
import edu.vinu.domain.chapter.response.ChapterResponse;
import edu.vinu.domain.chapter.response.ChapterStatCountResponse;
import edu.vinu.domain.chapter.service.ChapterService;
import edu.vinu.domain.lecture_record.response.LectureRecordResponse;
import edu.vinu.domain.resource.request.ResourceFilterRequest;
import edu.vinu.domain.resource.response.ResourceResponse;
import edu.vinu.domain.schedule_lecture.request.ScheduleLectureFilterRequest;
import edu.vinu.domain.schedule_lecture.response.ScheduleLectureResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequestMapping("/api/v2/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PostMapping
    public ResponseEntity<ApiResponse<ChapterResponse>> createChapter(@Valid @RequestBody ChapterCreateRequest request) {
        return ResponseEntity.ok(createApiResponse("Chapter created successfully", chapterService.createChapter(request)));
    }

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PatchMapping("{id}/details")
    public ResponseEntity<ApiResponse<ChapterResponse>> updateChapterDetails(@PathVariable("id") Long id, @Valid @RequestBody ChapterDetailsUpdateRequest request) {
        return ResponseEntity.ok(createApiResponse("Chapter details updated successfully", chapterService.updateChapterDetailsById(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('institute','teacher')")
    @PatchMapping("reorder")
    public ResponseEntity<ApiResponse<List<ChapterResponse>>> reorderChapters(@Valid @RequestBody ChapterReorderRequest request) {
        return ResponseEntity.ok(createApiResponse("Chapters reordered successfully!", chapterService.reorderChapters(request)));
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("{id}/detailed")
    public ResponseEntity<ApiResponse<ChapterDetailedResponse>> getDetailedChapterById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(createApiResponse("Chapter details fetched successfully", chapterService.getDetailedChapterById(id)));
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("{id}/lecture-records")
    public ResponseEntity<ApiResponse<List<LectureRecordResponse>>> getAllLectureRecordsById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(createApiResponse("All lecture records related to chapter fetched successfully!", chapterService.getAllLectureRecordsByChapterId(id)));
    }


    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("{id}/schedule-lectures")
    public ResponseEntity<PaginatedApiResponse<ScheduleLectureResponse>> getAllScheduleLecturesWithFilters(
            @PathVariable("id") Long chapterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "created_date") List<String> sortBy,
            ScheduleLectureFilterRequest filters
    ) {
        PaginatedApiResponse<ScheduleLectureResponse> response = chapterService.getAllScheduleLecturesByChapter(chapterId, page, size, direction, sortBy, filters);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("{id}/resources")
    public ResponseEntity<PaginatedApiResponse<ResourceResponse>> getAllResourcesWithFilters(
            @PathVariable("id") Long chapterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "created_date") List<String> sortBy,
            ResourceFilterRequest filters
    ) {
        PaginatedApiResponse<ResourceResponse> response = chapterService.getAllResourcesByChapter(chapterId, page, size, direction, sortBy, filters);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("{id}/assignments")
    public ResponseEntity<PaginatedApiResponse<ChapterAssignmentResponse>> getAllChapterAssignmentsWithFilters(
            @PathVariable("id") Long chapterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "created_date") List<String> sortBy,
            ChapterAssignmentFilterRequest filters
    ) {
        PaginatedApiResponse<ChapterAssignmentResponse> response = chapterService.getAllChapterAssignmentsByChapter(chapterId, page, size, direction, sortBy, filters);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("{id}/stats")
    public ResponseEntity<ApiResponse<ChapterStatCountResponse>> getStats(@PathVariable("id") Long id) {
        return ResponseEntity.ok(createApiResponse("Chapter stats fetched successfully!", chapterService.getStatCountByCourseId(id)));
    }
}
