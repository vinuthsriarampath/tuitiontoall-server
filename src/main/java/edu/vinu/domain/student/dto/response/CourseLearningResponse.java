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

package edu.vinu.domain.student.dto.response;

import edu.vinu.domain.course.enums.CourseCategory;
import edu.vinu.domain.course.enums.CourseLanguage;
import edu.vinu.domain.course.enums.CourseLevel;
import edu.vinu.domain.course.enums.CourseMode;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CourseLearningResponse(
        Long id,
        String title,
        String description,
        String thumbnail,
        CourseCategory category,
        CourseLevel level,
        CourseLanguage language,
        CourseMode mode,
        BigDecimal averageRating,
        Integer totalRatings,

        CurrentEnrollmentResponse currentEnrollment,

        boolean enrollmentHistoryAvailable
) { }
