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

package edu.vinu.domain.student.service.impl;

import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.student.dto.response.CourseLearningResponse;
import edu.vinu.domain.student.dto.response.CurrentEnrollmentResponse;
import edu.vinu.domain.student.dto.response.StudentLearningResponse;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.student.repository.projection.StudentLearningProjection;
import edu.vinu.domain.student.service.StudentLearningService;
import edu.vinu.domain.student.service.StudentService;
import edu.vinu.domain.student_batch_enrollment.enums.StudentBatchEnrollmentStatus;
import edu.vinu.domain.student_batch_enrollment.repository.StudentBatchEnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentLearningServiceImpl implements StudentLearningService {

    private final StudentService studentService;
    private final StudentBatchEnrollmentRepository enrollmentRepository;

    @Override
    public ApiResponse getMyLearningDetails() {

        StudentEntity currentStudent = studentService.getCurrentStudent();

        List<StudentLearningProjection> projections = enrollmentRepository.findStudentLearning(currentStudent.getId());

        Map<Long, List<StudentLearningProjection>> instituteGroups =
                projections.stream()
                        .collect(Collectors.groupingBy(
                                StudentLearningProjection::getInstituteId,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        List<StudentLearningResponse> institutes =
                instituteGroups.values()
                        .stream()
                        .map(this::buildInstituteResponse)
                        .toList();

        return ApiResponse.builder()
                .message("Student learning details retrieved successfully")
                .data(institutes)
                .build();


    }
    private StudentLearningResponse buildInstituteResponse(List<StudentLearningProjection> instituteRows){
        StudentLearningProjection first = instituteRows.get(0);

        Map<Long, List<StudentLearningProjection>> courseGroups =
                instituteRows.stream()
                        .collect(Collectors.groupingBy(
                                StudentLearningProjection::getCourseId,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        List<CourseLearningResponse> courses =
                courseGroups.values()
                        .stream()
                        .map(this::buildCourseResponse)
                        .toList();

        return StudentLearningResponse.builder()
                .instituteId(first.getInstituteId())
                .instituteName(first.getInstituteName())
                .courses(courses)
                .build();
    }

    private CourseLearningResponse buildCourseResponse(List<StudentLearningProjection> courseRows){
        StudentLearningProjection first = courseRows.get(0);

        StudentLearningProjection currentEnrollment = findCurrentEnrollment(courseRows);

        boolean historyAvailable = courseRows.size() > 1;

        return CourseLearningResponse.builder()
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
                .currentEnrollment(
                        buildCurrentEnrollmentResponse(currentEnrollment)
                )
                .enrollmentHistoryAvailable(historyAvailable)
                .build();

    }

    private StudentLearningProjection findCurrentEnrollment(List<StudentLearningProjection> courseRows) {
        return courseRows.stream()
                .filter(row -> row.getEnrollmentStatus() == StudentBatchEnrollmentStatus.ACTIVE)
                .findFirst()
                .orElse(
                        courseRows.stream()
                                .max(Comparator.comparing(
                                        StudentLearningProjection::getEnrollmentDate
                                ))
                                .orElseThrow()
                );
    }

    private CurrentEnrollmentResponse buildCurrentEnrollmentResponse(StudentLearningProjection projection) {
        return CurrentEnrollmentResponse.builder()
                .enrollmentId(projection.getEnrollmentId())
                .enrollmentStatus(projection.getEnrollmentStatus())
                .enrollmentDate(projection.getEnrollmentDate())
                .batchId(projection.getBatchId())
                .batchName(projection.getBatchName())
                .batchStatus(projection.getBatchStatus())
                .startDate(projection.getBatchStartDate())
                .build();
    }
}
