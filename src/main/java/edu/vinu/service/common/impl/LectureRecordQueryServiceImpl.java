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

import edu.vinu.mapper.LectureRecordMapper;
import edu.vinu.repository.LectureRecordRepository;
import edu.vinu.response.lecture_record.LectureRecordResponse;
import edu.vinu.service.common.LectureRecordQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureRecordQueryServiceImpl implements LectureRecordQueryService {
    private final LectureRecordRepository lectureRecordRepository;

    @Override
    public List<LectureRecordResponse> getAllLectureRecordsByChapterId(Long id) {
        return lectureRecordRepository.findAllByChapterId(id).stream().map(LectureRecordMapper::toLectureRecordResponse).toList();
    }
}
