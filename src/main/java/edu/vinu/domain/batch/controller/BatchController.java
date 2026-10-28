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

package edu.vinu.domain.batch.controller;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.batch.dto.Batch;
import edu.vinu.domain.batch.request.BatchCreateRequest;
import edu.vinu.domain.batch.request.BatchFilterRequest;
import edu.vinu.domain.batch.request.BatchStudentFilterRequest;
import edu.vinu.domain.batch.request.BatchUpdateRequest;
import edu.vinu.domain.batch.response.BatchDetailedResponse;
import edu.vinu.domain.batch.service.BatchService;
import edu.vinu.domain.module.request.ModuleFilterRequest;
import edu.vinu.domain.module.response.ModuleResponse;
import edu.vinu.domain.module.service.ModuleService;
import edu.vinu.domain.student.dto.response.StudentUserResponse;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static edu.vinu.common.util.ResponseUtil.createApiResponse;
import static edu.vinu.common.util.ResponseUtil.createPaginatedApiResponse;

@RestController
@RequestMapping("/api/v2/batches")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;
    private final ModuleService moduleService;
    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Batch>> createBatch(@Valid @RequestBody BatchCreateRequest request){
        return ResponseEntity.status(201).body(createApiResponse("Batch created successfully", batchService.createBatch(request)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<PaginatedApiResponse<Batch>> getAllBatchesOfCourse(@PathVariable Long courseId, PaginationRequest pagination, BatchFilterRequest filters){
        return ResponseEntity.ok(batchService.getAllBatchesByCourseId(courseId, pagination, filters));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/find/batch/{batchId}")
    public ResponseEntity<ApiResponse<Batch>> getBatchById(@PathVariable Long batchId){
        return ResponseEntity.status(200).body(createApiResponse("Batch By Batch Id",batchService.getBatchById(batchId)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{batchId}/update")
    public ResponseEntity<ApiResponse<Batch>> updateBatchById(@PathVariable Long batchId, @Valid @RequestBody BatchUpdateRequest request){
        return ResponseEntity.status(200).body(createApiResponse("Batch Updated Successfully",batchService.updateBatchById(batchId,request)));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/{batchId}/modules")
    public ResponseEntity<PaginatedApiResponse<ModuleResponse>> getModulesByBatch(
            @PathVariable Long batchId,
            PaginationRequest pagination,
            ModuleFilterRequest filters
    ){
        return ResponseEntity.status(200).body(createPaginatedApiResponse("Modules related to batch fetched successfully!", moduleService.getModulesByBatch(batchId,pagination,filters)));
    }

    @PreAuthorize("hasAnyAuthority('institute','student')")
    @GetMapping("{courseId}/enrollables")
    public ResponseEntity<ApiResponse<List<Batch>>> getAllEnrollableBatchesOfCourse(@PathVariable Long courseId){
        return ResponseEntity.status(200).body(batchService.getAllEnrollableBatchesOfCourse(courseId));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("{batchId}/students")
    public ResponseEntity<PaginatedApiResponse<StudentUserResponse>> getStudentsByBatch(@PathVariable Long batchId, PaginationRequest pagination, BatchStudentFilterRequest filters){
        PaginatedApiResponse<StudentUserResponse> response = enrollmentService.getStudentsByBatch(batchId, pagination, filters);
        return ResponseEntity.status(200).body(response);
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("detailed")
    public ResponseEntity<PaginatedApiResponse<BatchDetailedResponse>> getDetailedBatches(PaginationRequest pagination, BatchFilterRequest filters){
        return ResponseEntity.ok(batchService.getAllBatches(pagination,filters));
    }
}
