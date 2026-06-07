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

package edu.vinu.service.common.impl;

import edu.vinu.domain.chapter.mapper.ChapterMapper;
import edu.vinu.domain.chapter.repository.ChapterRepository;
import edu.vinu.response.chapter.ChapterResponse;
import edu.vinu.service.common.ChapterQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChapterQueryServiceImpl implements ChapterQueryService {

    private final ChapterRepository chapterRepository;
    @Override
    public List<ChapterResponse> getAllChaptersByModuleId(Long moduleId) {
        return chapterRepository.findAllByModuleIdOrderByChapterOrderAsc(moduleId).stream().map(ChapterMapper::toChapterResponse).toList();
    }
}
