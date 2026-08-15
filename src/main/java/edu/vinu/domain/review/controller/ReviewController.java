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

package edu.vinu.domain.review.controller;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.review.request.ReviewCreateRequest;
import edu.vinu.domain.review.response.BasicReviewResponse;
import edu.vinu.domain.review.response.ReviewEligibilityResponse;
import edu.vinu.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("hasAuthority('student')")
    @PostMapping
    public ResponseEntity<ApiResponse> createReview(@Valid @RequestBody ReviewCreateRequest request){
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyAuthority('student', 'institute')")
    public ResponseEntity<PaginatedApiResponse<BasicReviewResponse>> getReviewsByCourseId(@PathVariable Long courseId, PaginationRequest request){
        return ResponseEntity.ok(reviewService.getReviewsByCourseId(courseId, request));
    }

    @GetMapping("/course/{courseId}/eligibility")
    @PreAuthorize("hasAnyAuthority('student', 'institute')")
    public ResponseEntity<ReviewEligibilityResponse> checkReviewEligibility(@PathVariable Long courseId){
        return ResponseEntity.ok(reviewService.checkReviewEligibility(courseId));
    }
}
