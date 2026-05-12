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

package edu.vinu.controller;

import edu.vinu.request.modules.ModuleCreateRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.module.ModuleResponse;
import edu.vinu.service.common.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping
    public ResponseEntity<ApiResponse> createModule(@Valid @RequestBody ModuleCreateRequest request){
        ModuleResponse response = moduleService.createModule(request);
        return ResponseEntity.ok(new ApiResponse("Module created successfully", response));
    }
}
