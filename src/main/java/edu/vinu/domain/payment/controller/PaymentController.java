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

package edu.vinu.domain.payment.controller;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.domain.payment.dto.request.MyPaymentFilterRequest;
import edu.vinu.domain.payment.dto.request.MyPaymentReceivesFilterRequest;
import edu.vinu.domain.payment.dto.response.PaymentDetailedResponse;
import edu.vinu.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("my")
    public ResponseEntity<PaginatedApiResponse<PaymentDetailedResponse>> getMyPayments(PaginationRequest pagination, MyPaymentFilterRequest filters){
        return ResponseEntity.ok().body(paymentService.myPayments(pagination,filters));
    }

    @PreAuthorize("hasAuthority('institute')")
    @GetMapping("my/receives")
    public ResponseEntity<PaginatedApiResponse<PaymentDetailedResponse>> getMyReceivedPayments(PaginationRequest pagination, MyPaymentReceivesFilterRequest filters){
        return ResponseEntity.ok().body(paymentService.myPaymentsReceives(pagination,filters));
    }
}
