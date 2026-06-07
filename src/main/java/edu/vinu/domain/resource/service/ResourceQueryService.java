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

package edu.vinu.domain.resource.service;

import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.resource.request.ResourceFilterRequest;
import edu.vinu.domain.resource.response.ResourceResponse;

import java.util.List;

public interface ResourceQueryService {
    PaginatedApiResponse<ResourceResponse> getAllResourcesByChapter(Long chapterId, int page, int size, String direction, List<String> sortBy, ResourceFilterRequest filters);
}
