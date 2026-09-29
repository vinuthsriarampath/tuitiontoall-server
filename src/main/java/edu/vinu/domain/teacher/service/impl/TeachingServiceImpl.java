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

package edu.vinu.domain.teacher.service.impl;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.batch.response.BatchBasicResponse;
import edu.vinu.domain.institute.repository.InstituteTeacherRepository;
import edu.vinu.domain.teacher.dtos.response.CourseTeachingResponse;
import edu.vinu.domain.teacher.dtos.response.TeachingResponse;
import edu.vinu.domain.teacher.entity.TeacherEntity;
import edu.vinu.domain.teacher.repository.projection.TeachingProjection;
import edu.vinu.domain.teacher.service.TeacherService;
import edu.vinu.domain.teacher.service.TeachingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeachingServiceImpl implements TeachingService {

    private final TeacherService teacherService;
    private final InstituteTeacherRepository instituteTeacherRepository;

    @Override
    public ApiResponse getMyTeachingDetails() {

        TeacherEntity currentTeacher = teacherService.getCurrentTeacher();

        List<TeachingProjection> projections = instituteTeacherRepository.getTeachingByTeacherId(currentTeacher.getId());

        Map<Long, List<TeachingProjection>> instituteGroups = projections.stream()
                .collect(Collectors.groupingBy(
                        TeachingProjection::getInstituteId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<TeachingResponse> response = instituteGroups.values()
                .stream()
                .map(this::buildInstituteResponse)
                .toList();

        return ApiResponse.builder()
                .message("Teaching details retrieved successfully")
                .data(response)
                .build();
    }

    private TeachingResponse buildInstituteResponse(List<TeachingProjection> instituteRows){
        TeachingProjection first =  instituteRows.get(0);

        Map<Long, List<TeachingProjection>> courseGroups = instituteRows.stream()
                .collect(Collectors.groupingBy(
                        TeachingProjection::getCourseId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
        List<CourseTeachingResponse> courses = courseGroups.values()
                .stream()
                .map(this::buildTeachingResponse)
                .toList();

        return TeachingResponse.builder()
                .instituteId(first.getInstituteId())
                .instituteName(first.getInstituteName())
                .courses(courses)
                .build();
    }

    private CourseTeachingResponse buildTeachingResponse(List<TeachingProjection> courseRows){
        TeachingProjection first = courseRows.get(0);

        Map<Long, List<TeachingProjection>> batchGroups = courseRows.stream()
                .collect(Collectors.groupingBy(
                        TeachingProjection::getBatchId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<BatchBasicResponse> assignedBatches = batchGroups.values()
                .stream()
                .map(this::buildBatchResponse)
                .toList();

        return CourseTeachingResponse.builder()
                .id(first.getCourseId())
                .title(first.getCourseTitle())
                .description(first.getCourseDescription())
                .thumbnail(first.getThumbnail())
                .category(first.getCourseCategory())
                .level(first.getCourseLevel())
                .language(first.getCourseLanguage())
                .mode(first.getCourseMode())
                .averageRating(first.getAvgRating())
                .totalRatings(first.getTotalRatings())
                .AssignedBatches(assignedBatches)
                .build();
    }

    private BatchBasicResponse buildBatchResponse(List<TeachingProjection> batchRows){
        TeachingProjection first = batchRows.get(0);

        return BatchBasicResponse.builder()
                .id(first.getBatchId())
                .courseId(first.getCourseId())
                .name(first.getBatchName())
                .status(first.getBatchStatus())
                .enrollmentStatus(first.getBatchEnrollmentStatus())
                .createdDate(first.getBatchCreatedDate())
                .lastModifiedDate(first.getBatchLastModifiedDate())
                .build();
    }
}
