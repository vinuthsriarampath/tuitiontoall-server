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

import edu.vinu.enums.ModuleStatus;
import edu.vinu.request.modules.*;
import edu.vinu.response.module.ModuleDetailedResponse;
import edu.vinu.response.module.ModuleResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ModuleService {
    ModuleResponse createModule(ModuleCreateRequest request);

    ModuleResponse updateModuleName(Long id,ModuleNameUpdateRequest request);

    ModuleResponse publishModule(Long id);

    ModuleResponse lockModule(Long id);

    ModuleResponse archiveModule(Long id);

    Page<ModuleResponse> getAllFilteredModules(int page, int size, String direction, List<String> sortBy, ModuleFilterRequest filter);

    ModuleResponse updateModuleTeacher(Long id, ModuleTeacherUpdateRequest request);

    ModuleResponse updateModuleBatch(Long id, ModuleBatchUpdateRequest request);

    ModuleDetailedResponse getDetailedModuleById(Long id);
}
