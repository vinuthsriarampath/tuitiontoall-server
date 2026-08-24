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

import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.payment.repository.PaymentRepository;
import edu.vinu.domain.payment.service.PaymentStatService;
import edu.vinu.domain.reporting.enums.ReportingPeriod;
import edu.vinu.domain.reporting.enums.TrendType;
import edu.vinu.domain.reporting.projection.TrendPointProjection;
import edu.vinu.domain.reporting.response.ReportingPeriodRange;
import edu.vinu.domain.reporting.response.TrendPoint;
import edu.vinu.domain.reporting.utility.TrendBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentStatServiceImpl implements PaymentStatService {

    private final PaymentRepository paymentRepository;
    @Override
    public BigDecimal sumPaymentsByInstituteIdAndPaymentStatus(Long instituteId, PaymentStatus paymentStatus) {
        BigDecimal sum = paymentRepository.sumRevenueByInstituteAndStatus(instituteId, paymentStatus.name());
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal sumPaymentsByInstituteIdAndPaymentStatusBetween(Long instituteId, PaymentStatus paymentStatus, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        BigDecimal sum = paymentRepository.sumRevenueByInstituteAndStatusBetween(instituteId, paymentStatus.name(), startDateTime, endDateTime);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    public List<TrendPoint> getRevenueTrendByInstituteAndPaymentStatus(Long instituteId, PaymentStatus paymentStatus, ReportingPeriod period, ReportingPeriodRange range) {
        return switch (period) {
            case TODAY -> getHourlyRevenueTrend(instituteId, paymentStatus, range);
            case CURRENT_WEEK, CURRENT_MONTH, CURRENT_3_MONTHS -> getDailyRevenueTrend(instituteId, paymentStatus, range);
            case CURRENT_YEAR, OVERALL -> getMonthlyRevenueTrend(instituteId, paymentStatus, range);
        };
    }

    private List<TrendPoint> getHourlyRevenueTrend(Long instituteId, PaymentStatus paymentStatus, ReportingPeriodRange range){
        List<TrendPointProjection> projections = paymentRepository.getHourlyPaymentTrendsByInstituteAndStatus(instituteId, paymentStatus.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(), range.currentEnd(), TrendType.HOUR, projections);
    }

    private List<TrendPoint> getDailyRevenueTrend(Long instituteId, PaymentStatus paymentStatus, ReportingPeriodRange range){
        List<TrendPointProjection> projections = paymentRepository.getDailyPaymentTrendsByInstituteAndStatus(instituteId, paymentStatus.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(), range.currentEnd(), TrendType.DAY, projections);
    }

    private List<TrendPoint> getMonthlyRevenueTrend(Long instituteId, PaymentStatus paymentStatus, ReportingPeriodRange range){
        List<TrendPointProjection> projections = paymentRepository.getMonthlyPaymentTrendsByInstituteAndStatus(instituteId, paymentStatus.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(), range.currentEnd(), TrendType.MONTH, projections);
    }
}
