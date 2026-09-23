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

package edu.vinu.domain.student_batch_enrollment.mapper;

import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.student.dto.response.RecentEnrollmentResponse;
import edu.vinu.domain.student_batch_enrollment.dto.respose.EnrollmentResponse;
import edu.vinu.domain.student_batch_enrollment.entity.StudentBatchEnrollment;
import edu.vinu.domain.student_batch_enrollment.repository.projection.RecentEnrollmentProjection;

public class StudentBatchEnrollmentMapper {
    public static EnrollmentResponse toEnrollmentResponse(StudentBatchEnrollment studentBatchEnrollment, Payment payment) {
        return EnrollmentResponse.builder()
                .id(studentBatchEnrollment.getId())
                .studentId(studentBatchEnrollment.getStudent().getId())
                .instituteId(payment.getInstitute().getId())
                .batchId(studentBatchEnrollment.getBatch().getId())
                .batchEnrollmentStatus(studentBatchEnrollment.getStatus())
                .enrolledDate(studentBatchEnrollment.getCreatedDate())
                .paymentId(payment.getId())
                .paymentStatus(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .transactionRef(payment.getTransactionRef())
                .paymentDate(payment.getCreatedDate())
                .build();
    }

    public static RecentEnrollmentResponse toRecentEnrollmentResponse(RecentEnrollmentProjection p) {
        return RecentEnrollmentResponse.builder()
                .enrollmentId(p.getEnrollmentId())
                .courseId(p.getCourseId())
                .courseName(p.getCourseName())
                .batchId(p.getBatchId())
                .batchName(p.getBatchName())
                .enrollmentDate(p.getEnrollmentDate())
                .build();
    }
}
