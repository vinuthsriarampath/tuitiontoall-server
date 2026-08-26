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

package edu.vinu.domain.student_batch_enrollment.dto.respose;

import edu.vinu.domain.payment.enums.PaymentMethod;
import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.student_batch_enrollment.enums.StudentBatchEnrollmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EnrollmentResponse(
        Long id,
        Long studentId,
        Long instituteId,
        Long batchId,
        StudentBatchEnrollmentStatus batchEnrollmentStatus,
        LocalDateTime enrolledDate,
        Long paymentId,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        String transactionRef,
        LocalDateTime paymentDate
) {
}
