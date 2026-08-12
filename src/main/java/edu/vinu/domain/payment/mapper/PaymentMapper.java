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

package edu.vinu.domain.payment.mapper;

import edu.vinu.domain.payment.dto.response.PaymentResponse;
import edu.vinu.domain.payment.entity.Payment;

public class PaymentMapper {
    public static PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .studentId(payment.getStudent().getId())
                .instituteId(payment.getInstitute().getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .transactionRef(payment.getTransactionRef())
                .createdDate(payment.getCreatedDate())
                .lastModifiedDate(payment.getLastModifiedDate())
                .build();
    }
}
