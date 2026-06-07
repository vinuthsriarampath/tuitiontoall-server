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

import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.assignment.request.chapter_assignments.ChapterAssignmentFilterRequest;
import edu.vinu.domain.assignment.response.chapter_assignment.ChapterAssignmentResponse;
import edu.vinu.domain.chapter.entity.ChapterEntity;
import edu.vinu.domain.chapter.request.ChapterCreateRequest;
import edu.vinu.domain.chapter.request.ChapterDetailsUpdateRequest;
import edu.vinu.domain.chapter.request.ChapterReorderRequest;
import edu.vinu.domain.chapter.response.ChapterDetailedResponse;
import edu.vinu.domain.chapter.response.ChapterResponse;
import edu.vinu.domain.lecture_record.response.LectureRecordResponse;
import edu.vinu.domain.resource.request.ResourceFilterRequest;
import edu.vinu.domain.resource.response.ResourceResponse;
import edu.vinu.domain.schedule_lecture.request.ScheduleLectureFilterRequest;
import edu.vinu.domain.schedule_lecture.response.ScheduleLectureResponse;

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
