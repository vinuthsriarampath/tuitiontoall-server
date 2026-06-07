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

package edu.vinu.domain.resource.service.impl;

import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.resource.mapper.ResourceMapper;
import edu.vinu.domain.resource.repository.ResourceRepository;
import edu.vinu.domain.resource.request.ResourceFilterRequest;
import edu.vinu.domain.resource.response.ResourceResponse;
import edu.vinu.domain.resource.service.ResourceQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceQueryServiceImpl implements ResourceQueryService {
    private final ResourceRepository resourceRepository;
    @Override
    public PaginatedApiResponse<ResourceResponse> getAllResourcesByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ResourceFilterRequest filters) {
        Pageable pageable = PageRequest.of(page, size, SortUtil.buildSort(direction, sortBy, List.of("created_date")));
        Page<ResourceResponse> pageData = resourceRepository.getAllResourcesByChapter(chapterId, filters.resourceId() , filters.name(), pageable)
                .map(ResourceMapper::toResourceResponse);

        return PaginatedApiResponse.<ResourceResponse>builder()
                .message("All resources by chapter fetched successfully!")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
    }
}
