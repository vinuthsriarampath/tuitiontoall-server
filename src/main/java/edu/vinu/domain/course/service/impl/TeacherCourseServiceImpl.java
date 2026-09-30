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

package edu.vinu.domain.course.service.impl;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.batch.response.TeacherBatchResponse;
import edu.vinu.domain.course.repository.CourseRepository;
import edu.vinu.domain.course.repository.projections.TeacherCourseProjection;
import edu.vinu.domain.course.response.TeacherCourseResponse;
import edu.vinu.domain.course.response.TeacherCourseViewResponse;
import edu.vinu.domain.course.service.TeacherCourseService;
import edu.vinu.domain.module.response.TeacherModuleResponse;
import edu.vinu.domain.teacher.entity.TeacherEntity;
import edu.vinu.domain.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherCourseServiceImpl implements TeacherCourseService {

    private final TeacherService teacherService;
    private final CourseRepository courseRepository;

    @Override
    public ApiResponse getTeacherDetailedCourse(Long courseId) {
        TeacherEntity currentTeacher = teacherService.getCurrentTeacher();

        List<TeacherCourseProjection> projections = courseRepository.getTeacherCourse(courseId, currentTeacher.getId());

        TeacherCourseProjection first = projections.get(0);

        TeacherCourseResponse course = TeacherCourseResponse.builder()
                .id(first.getCourseId())
                .title(first.getCourseTitle())
                .description(first.getCourseDescription())
                .durationInHours(first.getCourseDurationInHours())
                .level(first.getCourseLevel())
                .category(first.getCourseCategory())
                .language(first.getCourseLanguage())
                .mode(first.getCourseMode())
                .thumbnail(first.getCourseThumbnail())
                .avgRating(first.getCourseAvgRating())
                .totalRatings(first.getCourseTotalRatings())
                .build();

        Map<Long,List<TeacherCourseProjection>> batchGroup = projections
                .stream()
                .collect(Collectors.groupingBy(
                        TeacherCourseProjection::getBatchId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<TeacherBatchResponse> batches = batchGroup.values()
                        .stream()
                        .map(this::buildBatchResponse)
                        .toList();


        TeacherCourseViewResponse response = TeacherCourseViewResponse.builder()
                .course(course)
                .batches(batches)
                .build();

        return ApiResponse.builder()
                .message("Teacher's course details retrieved successfully")
                .data(response)
                .build();
    }

    private TeacherBatchResponse buildBatchResponse(List<TeacherCourseProjection> batchRows){
        TeacherCourseProjection first = batchRows.get(0);

        List<TeacherModuleResponse> modules = batchRows.stream()
                .map(this::buildModuleResponse)
                .toList();

        return TeacherBatchResponse.builder()
                .batchId(first.getBatchId())
                .batchName(first.getBatchName())
                .batchStartDate(first.getBatchStartDate())
                .batchStartTime(first.getBatchStartTime())
                .batchStatus(first.getBatchStatus())
                .modules(modules)
                .build();
    }

    private TeacherModuleResponse buildModuleResponse(TeacherCourseProjection moduleRow){
        return TeacherModuleResponse.builder()
                .moduleId(moduleRow.getModuleId())
                .moduleName(moduleRow.getModuleName())
                .moduleStatus(moduleRow.getModuleStatus())
                .moduleCreatedDate(moduleRow.getModuleCreatedDate())
                .moduleLastModifiedDate(moduleRow.getModuleLastModifiedDate())
                .build();
    }
}
