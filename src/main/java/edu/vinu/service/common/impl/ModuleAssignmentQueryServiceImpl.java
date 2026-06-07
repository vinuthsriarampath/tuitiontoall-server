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

import edu.vinu.mapper.ModuleAssignmentMapper;
import edu.vinu.repository.ModuleAssignmentRepository;
import edu.vinu.request.assignments.module_assignments.ModuleAssignmentFilterRequest;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.response.assignments.module_assignment.ModuleAssignmentResponse;
import edu.vinu.service.common.ModuleAssignmentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleAssignmentQueryServiceImpl implements ModuleAssignmentQueryService {

    private final ModuleAssignmentRepository moduleAssignmentRepository;

    @Override
    public boolean existsByAssignmentId(Long assignmentId) {
        return moduleAssignmentRepository.existsByAssignmentId(assignmentId);
    }

    @Override
    public PaginatedApiResponse<ModuleAssignmentResponse> getAssignmentsByModule(Long moduleId, ModuleAssignmentFilterRequest filters, Pageable pageable) {
        Page<ModuleAssignmentResponse> pageData = moduleAssignmentRepository.getAllModuleAssignmentByModule(moduleId, filters.assignmentId(), filters.topic(), filters.reSubmission(),filters.lateSubmission(),filters.totalMarks(),filters.maxAttempts(),filters.availableOn(),filters.dueDate(),filters.createdDate(),filters.lastModifiedDate(),pageable).map(ModuleAssignmentMapper::toModuleAssignmentResponse);
        return PaginatedApiResponse.<ModuleAssignmentResponse>builder()
                .message("All assignments related to module fetched successfully!")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .last(pageData.isLast())
                .build();
    }
}
