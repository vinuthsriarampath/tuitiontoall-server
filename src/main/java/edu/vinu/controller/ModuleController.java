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

import edu.vinu.request.assignments.module_assignments.ModuleAssignmentFilterRequest;
import edu.vinu.request.modules.*;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.PaginatedApiResponse;
import edu.vinu.response.assignments.module_assignment.ModuleAssignmentResponse;
import edu.vinu.response.chapter.ChapterResponse;
import edu.vinu.response.module.ModuleDetailedResponse;
import edu.vinu.response.module.ModuleResponse;
import edu.vinu.service.common.ChapterQueryService;
import edu.vinu.service.common.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;
    private final ChapterQueryService chapterQueryService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping
    public ResponseEntity<ApiResponse> createModule(@Valid @RequestBody ModuleCreateRequest request){
        ModuleResponse response = moduleService.createModule(request);
        return ResponseEntity.ok(new ApiResponse("Module created successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse> updateModuleName(@PathVariable("id") Long id, @Valid @RequestBody ModuleNameUpdateRequest request){
        ModuleResponse response = moduleService.updateModuleName(id,request);
        return ResponseEntity.ok(new ApiResponse("Module updated successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/publish")
    public ResponseEntity<ApiResponse> publishModule(@PathVariable("id") Long id){
        ModuleResponse response = moduleService.publishModule(id);
        return ResponseEntity.ok(new ApiResponse("Module published successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/lock")
    public ResponseEntity<ApiResponse> lockModule(@PathVariable("id") Long id){
        ModuleResponse response = moduleService.lockModule(id);
        return ResponseEntity.ok(new ApiResponse("Module locked successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/archive")
    public ResponseEntity<ApiResponse> archiveModule(@PathVariable("id") Long id){
        ModuleResponse response = moduleService.archiveModule(id);
        return ResponseEntity.ok(new ApiResponse("Module archived successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/teacher")
    public ResponseEntity<ApiResponse> updateModuleTeacher(@PathVariable("id") Long id,@Valid @RequestBody ModuleTeacherUpdateRequest request){
        ModuleResponse response = moduleService.updateModuleTeacher(id, request);
        return ResponseEntity.ok(new ApiResponse("Module teacher updated successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("{id}/batch")
    public ResponseEntity<ApiResponse> updateModuleBatch(@PathVariable("id") Long id,@Valid @RequestBody ModuleBatchUpdateRequest request){
        ModuleResponse response = moduleService.updateModuleBatch(id, request);
        return ResponseEntity.ok(new ApiResponse("Module batch updated successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse> getModuleById(@PathVariable("id") Long id){
        ModuleResponse response = moduleService.getModuleById(id);
        return ResponseEntity.ok(new ApiResponse("Module fetched successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/{id}/detailed")
    public ResponseEntity<ApiResponse> getDetailedModuleById(@PathVariable("id") Long id){
        ModuleDetailedResponse response = moduleService.getDetailedModuleById(id);
        return ResponseEntity.ok(new ApiResponse("Module fetched successfully", response));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping
    public ResponseEntity<PaginatedApiResponse<ModuleResponse>> getAllModules(
            @RequestParam(value = "page",defaultValue = "0")int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "direction",defaultValue = "desc") String direction,
            @RequestParam(value = "sortBy",defaultValue = "created_date")List<String> sortBy,
            ModuleFilterRequest filter
            ){

        Page<ModuleResponse> pageData = moduleService.getAllFilteredModules(page, size, direction, sortBy, filter);
        PaginatedApiResponse<ModuleResponse> response = PaginatedApiResponse.<ModuleResponse>builder()
                .message("Modules retrieved successfully")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .last(pageData.isLast())
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/{id}/chapters/all")
    public ResponseEntity<ApiResponse> getChaptersByModuleId(@PathVariable("id") Long id){
        List<ChapterResponse> responses = chapterQueryService.getAllChaptersByModuleId(id);
        return ResponseEntity.ok(new ApiResponse("Chapters fetched successfully", responses));
    }

    @PreAuthorize("hasAuthority('institute')")
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
