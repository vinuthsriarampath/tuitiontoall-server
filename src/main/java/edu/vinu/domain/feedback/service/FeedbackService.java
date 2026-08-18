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

package edu.vinu.domain.feedback.service;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.feedback.request.FeedbackCreateRequest;
import edu.vinu.domain.feedback.response.FeedbackResponse;

public interface FeedbackService {
    ApiResponse submitFeedback(FeedbackCreateRequest request);

    PaginatedApiResponse<FeedbackResponse> getFeedbacksByCourse(Long courseId, PaginationRequest pagination);
}
