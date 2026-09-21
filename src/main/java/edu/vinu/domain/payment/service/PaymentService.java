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

package edu.vinu.domain.payment.service;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.payment.dto.request.MyPaymentFilterRequest;
import edu.vinu.domain.payment.dto.request.MyPaymentReceivesFilterRequest;
import edu.vinu.domain.payment.dto.response.PaymentDetailedResponse;
import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.student.entity.StudentEntity;

import java.math.BigDecimal;

public interface PaymentService {
    Payment pay(BigDecimal amount, StudentEntity studentEntity, InstituteEntity instituteEntity);

    PaginatedApiResponse<PaymentDetailedResponse> myPayments(PaginationRequest pagination, MyPaymentFilterRequest filters);

    PaginatedApiResponse<PaymentDetailedResponse> myPaymentsReceives(PaginationRequest pagination, MyPaymentReceivesFilterRequest filters);
}
