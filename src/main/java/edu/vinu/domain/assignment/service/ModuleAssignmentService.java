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

package edu.vinu.domain.assignment.service;

import edu.vinu.domain.assignment.request.module_assignments.ModuleAssignmentCreateRequest;
import edu.vinu.domain.assignment.response.module_assignment.ModuleAssignmentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ModuleAssignmentService {
    ModuleAssignmentResponse createModuleAssignment(ModuleAssignmentCreateRequest request, MultipartFile file);
}
