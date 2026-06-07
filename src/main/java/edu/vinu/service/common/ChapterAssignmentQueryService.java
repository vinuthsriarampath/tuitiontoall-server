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

package edu.vinu.service.common;

import edu.vinu.request.assignments.chapter_assignments.ChapterAssignmentFilterRequest;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.response.assignments.chapter_assignment.ChapterAssignmentResponse;

import java.util.List;

public interface ChapterAssignmentQueryService {
    PaginatedApiResponse<ChapterAssignmentResponse> getAllChapterAssignmentByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ChapterAssignmentFilterRequest filters);
    boolean existsByAssignmentId(Long assignmentId);
}
