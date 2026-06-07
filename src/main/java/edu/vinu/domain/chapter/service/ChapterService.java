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

package edu.vinu.domain.chapter.service;

import edu.vinu.domain.chapter.entity.ChapterEntity;
import edu.vinu.request.assignments.chapter_assignments.ChapterAssignmentFilterRequest;
import edu.vinu.domain.chapter.request.ChapterCreateRequest;
import edu.vinu.domain.chapter.request.ChapterDetailsUpdateRequest;
import edu.vinu.domain.chapter.request.ChapterReorderRequest;
import edu.vinu.request.resource.ResourceFilterRequest;
import edu.vinu.request.schedule_lecture.ScheduleLectureFilterRequest;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.response.assignments.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.domain.chapter.response.ChapterDetailedResponse;
import edu.vinu.domain.chapter.response.ChapterResponse;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import edu.vinu.response.resource.ResourceResponse;
import edu.vinu.response.schedule_lecture.ScheduleLectureResponse;

import java.util.List;

public interface ChapterService {
    ChapterResponse createChapter(ChapterCreateRequest request);

    ChapterResponse updateChapterDetailsById(Long id,ChapterDetailsUpdateRequest request);

    List<ChapterResponse> reorderChapters(ChapterReorderRequest request);

    ChapterDetailedResponse getDetailedChapterById(Long id);

    ChapterEntity getChapterEntityById(Long id);

    List<LectureRecordResponse> getAllLectureRecordsByChapterId(Long id);

    PaginatedApiResponse<ScheduleLectureResponse> getAllScheduleLecturesByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ScheduleLectureFilterRequest filters);

    PaginatedApiResponse<ResourceResponse> getAllResourcesByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ResourceFilterRequest filters);

    PaginatedApiResponse<ChapterAssignmentResponse> getAllChapterAssignmentsByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ChapterAssignmentFilterRequest filters);
}
