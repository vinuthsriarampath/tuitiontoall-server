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

import edu.vinu.entity.ChapterEntity;
import edu.vinu.request.chapter.ChapterCreateRequest;
import edu.vinu.request.chapter.ChapterDetailsUpdateRequest;
import edu.vinu.request.chapter.ChapterReorderRequest;
import edu.vinu.response.chapter.ChapterDetailedResponse;
import edu.vinu.response.chapter.ChapterResponse;
import edu.vinu.response.lecture_record.LectureRecordResponse;

import java.util.List;

public interface ChapterService {
    ChapterResponse createChapter(ChapterCreateRequest request);

    ChapterResponse updateChapterDetailsById(Long id,ChapterDetailsUpdateRequest request);

    List<ChapterResponse> reorderChapters(ChapterReorderRequest request);

    ChapterDetailedResponse getDetailedChapterById(Long id);

    ChapterEntity getChapterEntityById(Long id);

    List<LectureRecordResponse> getAllLectureRecordsByChapterId(Long id);
}
