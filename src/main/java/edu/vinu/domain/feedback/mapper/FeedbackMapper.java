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

package edu.vinu.domain.feedback.mapper;

import edu.vinu.domain.feedback.entity.Feedback;
import edu.vinu.domain.feedback.response.FeedbackResponse;

public class FeedbackMapper {
    public static FeedbackResponse toFeedbackResponse(Feedback e) {
        return FeedbackResponse.builder()
                .id(e.getId())
                .feedback(e.getFeedback())
                .courseId(e.getCourse().getId())
                .userId(e.getUser().getId())
                .createdDate(e.getCreatedDate())
                .lastModifiedDate(e.getLastModifiedDate())
                .build();
    }
}
