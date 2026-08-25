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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentDistributionResponse{
        @Builder.Default
        Long totalEnrollments = 0L;
        @Builder.Default
        Long activeEnrollments = 0L;
        @Builder.Default
        BigDecimal activeEnrollmentsPercentage = BigDecimal.ZERO;
        @Builder.Default
        Long completedEnrollments = 0L;
        @Builder.Default
        BigDecimal completedEnrollmentsPercentage = BigDecimal.ZERO;
        @Builder.Default
        Long suspendedEnrollments = 0L;
        @Builder.Default
        BigDecimal suspendedEnrollmentsPercentage = BigDecimal.ZERO;
}
