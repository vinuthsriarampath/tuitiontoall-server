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

package edu.vinu.domain.assignment.service.impl;

import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.assignment.mapper.ChapterAssignmentMapper;
import edu.vinu.domain.assignment.repository.ChapterAssignmentRepository;
import edu.vinu.domain.assignment.request.chapter_assignments.ChapterAssignmentFilterRequest;
import edu.vinu.domain.assignment.response.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.domain.assignment.service.ChapterAssignmentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterAssignmentQueryServiceImpl implements ChapterAssignmentQueryService {
    private final ChapterAssignmentRepository chapterAssignmentRepository;

    @Override
    public PaginatedApiResponse<ChapterAssignmentResponse> getAllChapterAssignmentByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ChapterAssignmentFilterRequest filters) {
        Pageable pageable = PageRequest.of(page, size, SortUtil.buildSort(direction, sortBy, List.of("created_date")));

        Page<ChapterAssignmentResponse> pageData = chapterAssignmentRepository.getAllChapterAssignmentByChapter(chapterId, filters.assignmentId(), filters.topic(), filters.reSubmission(),filters.lateSubmission(),filters.totalMarks(),filters.maxAttempts(),filters.availableOn(),filters.dueDate(),filters.createdDate(),filters.lastModifiedDate(),pageable).map(ChapterAssignmentMapper::toChapterAssignmentResponse);

        return PaginatedApiResponse.<ChapterAssignmentResponse>builder()
                .message("All assignments related to chapter fetched successfully!")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .last(pageData.isLast())
                .build();
    }

    @Override
    public boolean existsByAssignmentId(Long assignmentId) {
        return chapterAssignmentRepository.existsByAssignmentId(assignmentId);
    }
}
