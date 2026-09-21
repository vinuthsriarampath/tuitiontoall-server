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

import edu.vinu.common.exception.custom.NotFoundException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.course.mapper.StudentCourseMapper;
import edu.vinu.domain.course.repository.projections.StudentCourseProjection;
import edu.vinu.domain.course.response.StudentCourseViewResponse;
import edu.vinu.domain.course.service.StudentCourseService;
import edu.vinu.domain.module.enums.ModuleStatus;
import edu.vinu.domain.module.response.StudentModuleResponse;
import edu.vinu.domain.module.service.ModuleService;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.student.service.StudentService;
import edu.vinu.domain.student_batch_enrollment.repository.StudentBatchEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentCourseServiceImpl implements StudentCourseService {

    private final StudentService studentService;
    private final StudentBatchEnrollmentRepository enrollmentRepository;
    private final ModuleService moduleService;

    @Override
    public ApiResponse getStudentDetailedCourse(Long courseId, Long batchId) {
        StudentEntity currentStudent = studentService.getCurrentStudent();

        StudentCourseProjection studentCourseProjection = enrollmentRepository.findStudentCourse(courseId, batchId, currentStudent.getId()).orElseThrow(
                () -> new NotFoundException("Student is not enrolled in the specified course and batch.")
        );

        List<StudentModuleResponse> modules = moduleService.getStudentModulesByBatch(batchId, List.of(ModuleStatus.PUBLISHED, ModuleStatus.LOCKED));

        return ApiResponse.builder()
                .message("Student Course Details retrieved successfully!")
                .data(new StudentCourseViewResponse(
                        StudentCourseMapper.toStudentCourseResponse(studentCourseProjection),
                        StudentCourseMapper.toStudentBatchResponse(studentCourseProjection),
                        modules
                ))
                .build();
    }
}
