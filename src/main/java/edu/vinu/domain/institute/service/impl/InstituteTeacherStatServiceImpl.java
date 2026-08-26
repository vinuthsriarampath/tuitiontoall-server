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

package edu.vinu.domain.institute.service.impl;

import edu.vinu.domain.institute.enums.InstituteTeacherStatus;
import edu.vinu.domain.institute.repository.InstituteTeacherRepository;
import edu.vinu.domain.institute.service.InstituteTeacherStatService;
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
public class InstituteTeacherStatServiceImpl implements InstituteTeacherStatService {
    private final InstituteTeacherRepository instituteTeacherRepository;
    @Override
    public BigDecimal countInstituteTeachersByInstituteAndStatus(Long instituteId, InstituteTeacherStatus status) {
        Long count = instituteTeacherRepository.countByInstituteIdAndStatus(instituteId, status.name());
        return count != null ? BigDecimal.valueOf(count) : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal countActiveTeachersByInstituteAndStatusIdBetween(Long instituteId, InstituteTeacherStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Long count = instituteTeacherRepository.countByInstituteIdAndStatusBetween(instituteId, status.name(), startDateTime, endDateTime);
        return count != null ? BigDecimal.valueOf(count) : BigDecimal.ZERO;
    }

    @Override
    public List<TrendPoint> getInstituteTeachersTrends(Long instituteId, InstituteTeacherStatus status, ReportingPeriod period, ReportingPeriodRange range) {
        return switch (period) {
            case TODAY -> getHourlyInstituteTeachersTrend(instituteId, status, range);
            case CURRENT_WEEK, CURRENT_MONTH, CURRENT_3_MONTHS -> getDailyInstituteTeachersTrend(instituteId, status, range);
            case CURRENT_YEAR, OVERALL -> getMonthlyInstituteTeachersTrend(instituteId, status, range);
        };
    }

    private List<TrendPoint> getHourlyInstituteTeachersTrend(Long instituteId, InstituteTeacherStatus status, ReportingPeriodRange range) {
        List<TrendPointProjection> projections = instituteTeacherRepository.getHourlyInstituteTeachersTrendByInstituteAndStatus(instituteId, status.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(),range.currentEnd(), TrendType.HOUR, projections);
    }

    private List<TrendPoint> getDailyInstituteTeachersTrend(Long instituteId, InstituteTeacherStatus status, ReportingPeriodRange range) {
        List<TrendPointProjection> projections = instituteTeacherRepository.getDailyInstituteTeachersTrendByInstituteAndStatus(instituteId, status.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(),range.currentEnd(), TrendType.DAY, projections);
    }

    private List<TrendPoint> getMonthlyInstituteTeachersTrend(Long instituteId, InstituteTeacherStatus status, ReportingPeriodRange range) {
        List<TrendPointProjection> projections = instituteTeacherRepository.getMonthlyInstituteTeachersTrendByInstituteAndStatus(instituteId, status.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(),range.currentEnd(), TrendType.MONTH, projections);
    }


}
