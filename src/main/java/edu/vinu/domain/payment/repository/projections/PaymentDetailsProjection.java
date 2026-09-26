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

package edu.vinu.domain.payment.repository.projections;

import edu.vinu.domain.payment.enums.PaymentMethod;
import edu.vinu.domain.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentDetailsProjection {
    Long getPaymentId();
    Long getStudentId();
    String getStudentName();
    Long getInstituteId();
    String getInstituteName();
    BigDecimal getAmount();
    PaymentStatus getStatus();
    PaymentMethod getPaymentMethod();
    String getTransactionRef();
    LocalDateTime getCreatedDate();
    LocalDateTime getLastModifiedDate();
}
