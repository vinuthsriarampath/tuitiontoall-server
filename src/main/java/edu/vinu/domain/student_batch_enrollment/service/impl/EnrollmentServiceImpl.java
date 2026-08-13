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
import edu.vinu.domain.auth.service.UserAuthenticationService;
import edu.vinu.domain.batch.entity.BatchEntity;
import edu.vinu.domain.batch.enums.BatchEnrollmentStatus;
import edu.vinu.domain.batch.service.BatchService;
import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.course.service.CourseService;
import edu.vinu.domain.openPdf.service.InvoicePdfGeneratorService;
import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.payment.service.PaymentService;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentEligibilityCheckRequest;
import edu.vinu.domain.student_batch_enrollment.dto.request.EnrollmentRequest;
import edu.vinu.domain.student_batch_enrollment.dto.respose.EnrollmentEligibilityResponse;
import edu.vinu.domain.student_batch_enrollment.entity.StudentBatchEnrollment;
import edu.vinu.domain.student_batch_enrollment.enums.EnrollmentEligibilityReason;
import edu.vinu.domain.student_batch_enrollment.enums.StudentBatchEnrollmentStatus;
import edu.vinu.domain.student_batch_enrollment.repository.StudentBatchEnrollmentRepository;
import edu.vinu.domain.student_batch_enrollment.service.EnrollmentService;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.user.entity.UserEntity;
import edu.vinu.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final CourseService courseService;
    private final UserService userService;
    private final BatchService batchService;
    private final PaymentService paymentService;
    private final StudentBatchEnrollmentRepository  studentBatchEnrollmentRepository;
    private final InvoicePdfGeneratorService invoicePdfGenerator;
    private final UserAuthenticationService authService;

    @Override
    @Transactional
    public byte[] enrollStudent(EnrollmentRequest request) {
        UserEntity userEntity = userService.getUserEntityByEmail(authService.getCurrentUserEmail());
        StudentEntity studentEntity;

        if(userEntity.getRole().getRole().equals("student")){
            studentEntity = userEntity.getStudent();

            CourseEntity courseEntity = courseService.getCourseEntityById(request.courseId());
            BatchEntity batchEntity = batchService.getBatchEntityById(request.batchId());

            if(!Objects.equals(batchEntity.getCourse().getId(), courseEntity.getId())){
                throw new InvalidInputException("Batch does not belong to the course");
            }

            if (studentBatchEnrollmentRepository.countByStudentIdAndBatchId(studentEntity.getId(), batchEntity.getId()) > 0) {
                throw new InvalidInputException("Student is already enrolled in this batch");
            }

            if (studentBatchEnrollmentRepository.countActiveEnrollmentByStudentAndCourse(studentEntity.getId(), courseEntity.getId()) > 0) {
                throw new InvalidInputException("Student is already enrolled in another ACTIVE batch of this course");
            }

            if (Boolean.TRUE.equals(batchEntity.getIs_seat_limited())) {

                long enrolledStudents = studentBatchEnrollmentRepository.countEnrollmentsByBatchId(batchEntity.getId());

                if (enrolledStudents >= batchEntity.getMax_seat_limit()) {
                    throw new InvalidInputException("This batch has reached its maximum seat capacity");
                }
            }

            Payment payment = paymentService.pay(BigDecimal.valueOf(courseEntity.getPrice()),studentEntity, courseEntity.getInstitute());

            StudentBatchEnrollment studentBatchEnrollment = StudentBatchEnrollment.builder()
                    .student(studentEntity)
                    .batch(batchEntity)
                    .payment(payment)
                    .status(StudentBatchEnrollmentStatus.ACTIVE)
                    .build();

            StudentBatchEnrollment savedEnrollment;
            try {
                savedEnrollment = studentBatchEnrollmentRepository.save(studentBatchEnrollment);
            } catch (DataIntegrityViolationException e) {
                throw new InvalidInputException("Student has already been enrolled");
            }

            return invoicePdfGenerator.generate(savedEnrollment, payment);
        }
        throw new UnauthorizedException("Only Students can enroll with courses");
    }

    @Override
    public ApiResponse checkEnrollmentEligibility(EnrollmentEligibilityCheckRequest request) {
        CourseEntity course = courseService.getCourseEntityById(request.courseId());
        BatchEntity batch = batchService.getBatchEntityById(request.batchId());

        UserEntity userEntity = userService.getUserEntityByEmail(authService.getCurrentUserEmail());
        StudentEntity studentEntity;

        if(userEntity.getRole().getRole().equals("student")){
            studentEntity = userEntity.getStudent();
            if (!Objects.equals(batch.getCourse().getId(), course.getId())) {
                return ApiResponse.builder()
                        .message("The selected batch does not belong to this course")
                        .data(
                                EnrollmentEligibilityResponse.builder()
                                .canEnroll(false)
                                .reason(EnrollmentEligibilityReason.INVALID_BATCH)
                                .build()
                        )
                        .build();
            }

            if (batch.getEnrollment_status() != BatchEnrollmentStatus.OPEN) {
                return ApiResponse.builder()
                        .message("Enrollment for this batch is currently closed")
                        .data(
                                EnrollmentEligibilityResponse.builder()
                                        .canEnroll(false)
                                        .reason(EnrollmentEligibilityReason.ENROLLMENT_CLOSED)
                                        .build()
                        )
                        .build();
            }

            if (studentBatchEnrollmentRepository.countByStudentIdAndBatchId(studentEntity.getId(), request.batchId()) > 0) {
                return ApiResponse.builder()
                        .message("Student is already enrolled in this batch")
                        .data(
                                EnrollmentEligibilityResponse.builder()
                                        .canEnroll(false)
                                        .reason(EnrollmentEligibilityReason.ALREADY_ENROLLED)
                                        .build()
                        )
                        .build();
            }

            if (studentBatchEnrollmentRepository.countActiveEnrollmentByStudentAndCourse(studentEntity.getId(), request.courseId()) > 0) {
                return ApiResponse.builder()
                        .message("Student is already enrolled in another active batch of this course")
                        .data(
                                EnrollmentEligibilityResponse.builder()
                                        .canEnroll(false)
                                        .reason(EnrollmentEligibilityReason.ANOTHER_ACTIVE_BATCH)
                                        .build()
                        )
                        .build();
            }

            if (Boolean.TRUE.equals(batch.getIs_seat_limited())) {

                long enrolledStudents = studentBatchEnrollmentRepository.countEnrollmentsByBatchId(request.batchId());

                if (enrolledStudents >= batch.getMax_seat_limit()) {
                    return ApiResponse.builder()
                            .message("This batch has reached its maximum seat capacity")
                            .data(
                                    EnrollmentEligibilityResponse.builder()
                                            .canEnroll(false)
                                            .reason(EnrollmentEligibilityReason.BATCH_FULL)
                                            .build()
                            )
                            .build();
                }
            }

            return ApiResponse.builder()
                    .message("Student can enroll in this batch")
                    .data(
                            EnrollmentEligibilityResponse.builder()
                                    .canEnroll(true)
                                    .reason(EnrollmentEligibilityReason.ELIGIBLE)
                                    .build()
                    )
                    .build();
        }
        throw new UnauthorizedException("Only Students can check enrollment eligibility");
    }
}
