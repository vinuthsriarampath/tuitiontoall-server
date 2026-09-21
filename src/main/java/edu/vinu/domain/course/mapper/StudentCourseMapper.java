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

package edu.vinu.domain.course.mapper;

import edu.vinu.domain.batch.response.StudentBatchResponse;
import edu.vinu.domain.course.repository.projections.StudentCourseProjection;
import edu.vinu.domain.course.response.StudentCourseResponse;

public class StudentCourseMapper {
    public static StudentCourseResponse toStudentCourseResponse(StudentCourseProjection p){
        return StudentCourseResponse.builder()
                .id(p.getCourseId())
                .title(p.getCourseTitle())
                .description(p.getCourseDescription())
                .durationInHours(p.getDurationInHours())
                .level(p.getCourseLevel())
                .category(p.getCourseCategory())
                .language(p.getCourseLanguage())
                .mode(p.getCourseMode())
                .thumbnail(p.getThumbnail())
                .avgRating(p.getAvgRating())
                .totalRatings(p.getTotalRatings())
                .build();
    }

    public static StudentBatchResponse toStudentBatchResponse(StudentCourseProjection p){
        return StudentBatchResponse.builder()
                .id(p.getBatchId())
                .courseId(p.getCourseId())
                .name(p.getBatchName())
                .startDate(p.getStartDate())
                .startTime(p.getStartTime())
                .status(p.getBatchStatus())
                .build();
    }
}
