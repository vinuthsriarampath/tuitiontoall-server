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

import edu.vinu.request.assignments.AssignmentUpdateRequest;
import edu.vinu.response.ApiResponse;
import edu.vinu.response.assignments.AssignmentDetailedResponse;
import edu.vinu.service.common.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse> updateAssignment(@PathVariable Long id, @Valid @RequestBody AssignmentUpdateRequest request){
        AssignmentDetailedResponse response = assignmentService.updateAssignment(id,request);
        return ResponseEntity.ok(new ApiResponse("Assignment updated successfully!",response));
    }
}
