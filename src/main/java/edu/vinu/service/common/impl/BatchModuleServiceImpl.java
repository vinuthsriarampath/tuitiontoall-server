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

import edu.vinu.domain.module.request.ModuleFilterRequest;
import edu.vinu.domain.module.response.ModuleResponse;
import edu.vinu.service.common.BatchModuleService;
import edu.vinu.service.common.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchModuleServiceImpl implements BatchModuleService {

    private final ModuleService moduleService;

    @Override
    public Page<ModuleResponse> getAllModulesByBatch(Long batchId, int page, int size, String direction, List<String> sortBy) {
        ModuleFilterRequest filter= new ModuleFilterRequest(null,batchId);
        return this.moduleService.getAllFilteredModules(page, size, direction, sortBy, filter);
    }
}
