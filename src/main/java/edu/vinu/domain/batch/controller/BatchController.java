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
import edu.vinu.domain.batch.request.BatchUpdateRequest;
import edu.vinu.domain.batch.service.BatchService;
import edu.vinu.domain.module.response.ModuleResponse;
import edu.vinu.domain.module.service.ModuleQueryService;
import edu.vinu.domain.student.dto.response.StudentUserResponse;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/batches")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;
    private final ModuleQueryService moduleQueryService;
    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasAuthority('institute')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createBatch(@Valid @RequestBody BatchCreateRequest request){
        Batch batch = batchService.createBatch(request);
        return ResponseEntity.status(201).body(new ApiResponse("Batch created successfully", batch));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/{courseId}/all")
    public ResponseEntity<ApiResponse> getAllBatchesOfCourse(@PathVariable Long courseId){
        List<Batch> batches = batchService.getAllBatchesByCourseId(courseId);
        return ResponseEntity.status(200).body(new ApiResponse("All Batches Related to Course ID",batches));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/find/batch/{batchId}")
    public ResponseEntity<ApiResponse> getBatchById(@PathVariable Long batchId){
        Batch batch= batchService.getBatchById(batchId);
        return ResponseEntity.status(200).body(new ApiResponse("Batch By Batch Id",batch));
    }

    @PreAuthorize("hasAuthority('institute')")
    @PatchMapping("/{batchId}/update")
    public ResponseEntity<ApiResponse> updateBatchById(@PathVariable Long batchId, @Valid @RequestBody BatchUpdateRequest request){
        Batch  batch = batchService.updateBatchById(batchId,request);
        return ResponseEntity.status(200).body(new ApiResponse("Batch Updated Successfully",batch));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("/{id}/modules")
    public ResponseEntity<PaginatedApiResponse<ModuleResponse>> getBatchFullDetailsById(
            @PathVariable Long id,
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "10") int size,
            @RequestParam(value = "direction",defaultValue = "desc") String direction,
            @RequestParam(value = "sortBy",defaultValue = "created_date") List<String> sortBy
    ){
        Page<ModuleResponse> pageData = moduleQueryService.getAllModulesByBatch(id,page,size,direction,sortBy);
        PaginatedApiResponse<ModuleResponse> response = PaginatedApiResponse.<ModuleResponse>builder()
                .message("Modules related to batch fetched successfully!")
                .data(pageData.getContent())
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @PreAuthorize("hasAnyAuthority('institute','student')")
    @GetMapping("{courseId}/enrollables")
    public ResponseEntity<ApiResponse> getAllEnrollableBatchesOfCourse(@PathVariable Long courseId){
        ApiResponse response = batchService.getAllEnrollableBatchesOfCourse(courseId);
        return ResponseEntity.status(200).body(response);
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("{batchId}/students")
    public ResponseEntity<PaginatedApiResponse<StudentUserResponse>> getStudentsByBatch(@PathVariable Long batchId, PaginationRequest pagination){
        PaginatedApiResponse<StudentUserResponse> response = enrollmentService.getStudentsByBatch(batchId, pagination);
        return ResponseEntity.status(200).body(response);
    }
}
