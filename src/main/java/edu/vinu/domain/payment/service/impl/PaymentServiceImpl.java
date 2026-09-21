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

package edu.vinu.domain.payment.service.impl;

import edu.vinu.common.dto.PaginationRequest;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.common.response.PaginatedApiResponse;
import edu.vinu.common.util.SortUtil;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.institute.service.InstituteService;
import edu.vinu.domain.payment.dto.request.MyPaymentFilterRequest;
import edu.vinu.domain.payment.dto.request.MyPaymentReceivesFilterRequest;
import edu.vinu.domain.payment.dto.response.PaymentDetailedResponse;
import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.payment.enums.PaymentMethod;
import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.payment.mapper.PaymentMapper;
import edu.vinu.domain.payment.repository.PaymentRepository;
import edu.vinu.domain.payment.service.PaymentService;
import edu.vinu.domain.student.entity.StudentEntity;
import edu.vinu.domain.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final StudentService studentService;
    private final InstituteService instituteService;
    @Override
    public Payment pay(BigDecimal amount, StudentEntity studentEntity, InstituteEntity instituteEntity) {
        Payment paymentEntity = Payment.builder()
                .amount(amount)
                .status(PaymentStatus.PAID)
                .paymentMethod(PaymentMethod.CARD)
                .student(studentEntity)
                .institute(instituteEntity)
                .build();

        try {
            return paymentRepository.save(paymentEntity);
        }catch (DataIntegrityViolationException e) {
            throw new InvalidInputException("Duplicate Payment!");
        }
    }

    @Override
    public PaginatedApiResponse<PaymentDetailedResponse> myPayments(PaginationRequest pagination, MyPaymentFilterRequest filters) {
        StudentEntity currentStudent = studentService.getCurrentStudent();

        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(),pagination.sortBy(), List.of("created_date")));

        Page<PaymentDetailedResponse> page = paymentRepository.getMyPayments(
                currentStudent.getId(),
                filters.id(),
                filters.instituteId(),
                filters.instituteName(),
                filters.status() != null ? filters.status().name() : null,
                filters.paymentMethod() != null ? filters.paymentMethod().name() : null,
                filters.transactionRef(),
                filters.createdDate(),
                pageable
        ).map(PaymentMapper::toPaymentDetailedResponse);

        return PaginatedApiResponse.<PaymentDetailedResponse>builder()
                .message("Payments retrieved successfully")
                .data(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public PaginatedApiResponse<PaymentDetailedResponse> myPaymentsReceives(PaginationRequest pagination, MyPaymentReceivesFilterRequest filters) {
        InstituteEntity currentInstitute = instituteService.getCurrentInstitute();

        Pageable pageable = PageRequest.of(pagination.page(), pagination.size(), SortUtil.buildSort(pagination.direction(),pagination.sortBy(), List.of("created_date")));

        Page<PaymentDetailedResponse> page = paymentRepository.getMyReceives(
                currentInstitute.getId(),
                filters.id(),
                filters.studentId(),
                filters.studentName(),
                filters.status() != null ? filters.status().name() : null,
                filters.paymentMethod() != null ? filters.paymentMethod().name() : null,
                filters.transactionRef(),
                filters.createdDate(),
                pageable
        ).map(PaymentMapper::toPaymentDetailedResponse);


        return PaginatedApiResponse.<PaymentDetailedResponse>builder()
                .message("Payments receives retrieved successfully")
                .data(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
