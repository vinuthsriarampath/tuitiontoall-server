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

package edu.vinu.domain.module.controller;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.assignment.request.module_assignments.ModuleAssignmentFilterRequest;
import edu.vinu.domain.assignment.response.module_assignment.ModuleAssignmentResponse;
import edu.vinu.domain.chapter.response.ChapterResponse;
import edu.vinu.domain.chapter.service.ChapterQueryService;
import edu.vinu.domain.module.request.ModuleFilterRequest;
import edu.vinu.domain.module.request.create.ModuleCreateRequest;
import edu.vinu.domain.module.request.update.ModuleBatchUpdateRequest;
import edu.vinu.domain.module.request.update.ModuleNameUpdateRequest;
import edu.vinu.domain.module.request.update.ModuleTeacherUpdateRequest;
import edu.vinu.domain.module.response.ModuleDetailedResponse;
import edu.vinu.domain.module.response.ModuleResponse;
import edu.vinu.domain.module.service.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;

@RestController
@RequestMapping("/api/v2/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;
    private final ChapterQueryService chapterQueryService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping
    public ResponseEntity<ApiResponse<ModuleResponse>> createModule(@Valid @RequestBody ModuleCreateRequest request){
        return ResponseEntity.ok(createApiResponse("Module created successfully", moduleService.createModule(request)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<ModuleResponse>> updateModuleName(@PathVariable("id") Long id, @Valid @RequestBody ModuleNameUpdateRequest request){
        return ResponseEntity.ok(createApiResponse("Module updated successfully", moduleService.updateModuleName(id,request)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/publish")
    public ResponseEntity<ApiResponse<ModuleResponse>> publishModule(@PathVariable("id") Long id){
        return ResponseEntity.ok(createApiResponse("Module published successfully", moduleService.publishModule(id)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/lock")
    public ResponseEntity<ApiResponse<ModuleResponse>> lockModule(@PathVariable("id") Long id){
        return ResponseEntity.ok(createApiResponse("Module locked successfully", moduleService.lockModule(id)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/archive")
    public ResponseEntity<ApiResponse<ModuleResponse>> archiveModule(@PathVariable("id") Long id){
        return ResponseEntity.ok(createApiResponse("Module archived successfully", moduleService.archiveModule(id)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/teacher")
    public ResponseEntity<ApiResponse<ModuleResponse>> updateModuleTeacher(@PathVariable("id") Long id,@Valid @RequestBody ModuleTeacherUpdateRequest request){
        return ResponseEntity.ok(createApiResponse("Module teacher updated successfully", moduleService.updateModuleTeacher(id, request)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/batch")
    public ResponseEntity<ApiResponse<ModuleResponse>> updateModuleBatch(@PathVariable("id") Long id,@Valid @RequestBody ModuleBatchUpdateRequest request){
        return ResponseEntity.ok(createApiResponse("Module batch updated successfully", moduleService.updateModuleBatch(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ModuleResponse>> getModuleById(@PathVariable("id") Long id){
        return ResponseEntity.ok(createApiResponse("Module fetched successfully", moduleService.getModuleById(id)));
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("/{id}/detailed")
    public ResponseEntity<ApiResponse<ModuleDetailedResponse>> getDetailedModuleById(@PathVariable("id") Long id){
        return ResponseEntity.ok(createApiResponse("Module fetched successfully", moduleService.getDetailedModuleById(id)));
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("/{id}/chapters/all")
    public ResponseEntity<ApiResponse<List<ChapterResponse>>> getChaptersByModuleId(@PathVariable("id") Long id){
        return ResponseEntity.ok(createApiResponse("Chapters fetched successfully", chapterQueryService.getAllChaptersByModuleId(id)));
    }

    @PreAuthorize("hasAnyAuthority('institute','student','teacher')")
    @GetMapping("{id}/assignments")
    public ResponseEntity<PaginatedApiResponse<ModuleAssignmentResponse>> getAssignmentsByModule(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "created_date")List<String> sortBy,
            ModuleAssignmentFilterRequest filters
    ){
        PaginatedApiResponse<ModuleAssignmentResponse> responses = moduleService.getAssignmentsByModule(id,page,size,direction,sortBy,filters);
        return ResponseEntity.ok(responses);
    }
}
