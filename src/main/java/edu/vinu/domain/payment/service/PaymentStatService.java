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

import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.reporting.enums.ReportingPeriod;
import edu.vinu.domain.reporting.response.ReportingPeriodRange;
import edu.vinu.domain.reporting.response.TrendPoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentStatService {
    BigDecimal sumPaymentsByInstituteIdAndPaymentStatus(Long instituteId, PaymentStatus paymentStatus);

    BigDecimal sumPaymentsByInstituteIdAndPaymentStatusBetween(Long instituteId, PaymentStatus paymentStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<TrendPoint> getRevenueTrendByInstituteAndPaymentStatus(Long instituteId, PaymentStatus paymentStatus, ReportingPeriod period, ReportingPeriodRange range);
}
