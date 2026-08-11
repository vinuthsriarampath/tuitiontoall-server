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

package edu.vinu.domain.student_batch_enrollment.service.impl;

import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.exception.custom.UnauthorizedException;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.domain.batch.entity.BatchEntity;
import edu.vinu.domain.batch.service.BatchService;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.payment.service.PaymentService;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentRequest;
import edu.vinu.domain.student_batch_enrollment.entity.StudentBatchEnrollment;
import edu.vinu.domain.student_batch_enrollment.enums.StudentBatchEnrollmentStatus;
import edu.vinu.domain.student_batch_enrollment.mapper.StudentBatchEnrollmentMapper;
import edu.vinu.domain.student_batch_enrollment.repository.StudentBatchEnrollmentRepository;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import edu.vinu.domain.user.entity.StudentEntity;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final CourseService courseService;
    private final UserService userService;
    private final BatchService batchService;
    private final PaymentService paymentService;
    private final StudentBatchEnrollmentRepository  studentBatchEnrollmentRepository;

    @Override
    @Transactional
    public ApiResponse enrollStudent(EnrollmentRequest request) {
        UserEntity userEntity = userService.getUserEntityByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        StudentEntity studentEntity;

        if(userEntity.getRole().getRole().equals("student")){
            studentEntity = userEntity.getStudent();

            CourseEntity courseEntity = courseService.getCourseEntityById(request.courseId());
            BatchEntity batchEntity = batchService.getBatchEntityById(request.batchId());

            if(!Objects.equals(batchEntity.getCourse().getId(), courseEntity.getId())){
                throw new InvalidInputException("Batch does not belong to the course");
            }

            Payment payment = paymentService.pay(studentEntity, courseEntity, courseEntity.getInstitute());

            StudentBatchEnrollment studentBatchEnrollment = StudentBatchEnrollment.builder()
                    .student(studentEntity)
                    .batch(batchEntity)
                    .payment(payment)
                    .status(StudentBatchEnrollmentStatus.ACTIVE)
                    .build();

            StudentBatchEnrollment savedEnrollment = studentBatchEnrollmentRepository.save(studentBatchEnrollment);

            return ApiResponse.builder()
                    .message("Student Enrolled Successfully")
                    .data(StudentBatchEnrollmentMapper.toEnrollmentResponse(savedEnrollment, payment))
                    .build();
        }
        throw new UnauthorizedException("Only Students can enroll with courses");
    }
}
