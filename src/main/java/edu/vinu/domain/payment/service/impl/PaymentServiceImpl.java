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

import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.payment.enums.PaymentMethod;
import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.payment.repository.PaymentRepository;
import edu.vinu.domain.payment.service.PaymentService;
import edu.vinu.domain.student.entity.StudentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
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
}
