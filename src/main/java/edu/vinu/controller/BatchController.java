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

import edu.vinu.model.Batch;
import edu.vinu.request.BatchCreateRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.service.common.BatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/batches")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;

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
}
