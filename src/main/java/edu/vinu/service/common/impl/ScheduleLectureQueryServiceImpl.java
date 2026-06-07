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

import edu.vinu.mapper.ScheduleLectureMapper;
import edu.vinu.repository.ScheduleLectureRepository;
import edu.vinu.request.schedule_lecture.ScheduleLectureFilterRequest;
import edu.vinu.response.PaginatedApiResponse;
import edu.vinu.response.schedule_lecture.ScheduleLectureResponse;
import edu.vinu.service.common.ScheduleLectureQueryService;
import edu.vinu.common.util.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleLectureQueryServiceImpl implements ScheduleLectureQueryService {
    private final ScheduleLectureRepository scheduleLectureRepository;
    @Override
    public PaginatedApiResponse<ScheduleLectureResponse> getAllScheduleLecturesByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ScheduleLectureFilterRequest filters) {
        Pageable pageable = PageRequest.of(page,size, SortUtil.buildSort(direction, sortBy, List.of("created_date")));
        Page<ScheduleLectureResponse> pageData = scheduleLectureRepository.getAllModules(chapterId, filters.scheduleLectureId(), filters.startDate(), filters.startTime(), filters.endTime(), filters.status(), pageable)
                .map(ScheduleLectureMapper::toScheduleLectureResponse);

        return PaginatedApiResponse.<ScheduleLectureResponse>builder()
                .message("Schedule Lectures retrieved successfully")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
    }
}
