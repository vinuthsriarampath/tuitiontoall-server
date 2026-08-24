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

package edu.vinu.domain.batch.service.impl;

import edu.vinu.domain.batch.enums.BatchStatus;
import edu.vinu.domain.batch.repository.BatchRepository;
import edu.vinu.domain.batch.service.BatchStatService;
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
public class BatchStatServiceImpl implements BatchStatService {

    private final BatchRepository batchRepository;
    @Override
    public BigDecimal countBatchesByInstituteAndBatchStatus(Long instituteId, BatchStatus batchStatus) {
        Long count =  batchRepository.countBatchesByInstituteIdAndBatchStatus(instituteId, batchStatus.name());
        return count != null ? BigDecimal.valueOf(count) : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal countBatchesByInstituteAndBatchStatusBetween(Long instituteId, BatchStatus batchStatus, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Long count = batchRepository.countBatchesByInstituteIdAndBatchStatusBetween(instituteId, batchStatus.name(), startDateTime, endDateTime);
        return count != null ? BigDecimal.valueOf(count) : BigDecimal.ZERO;
    }

    @Override
    public List<TrendPoint> getBatchesTrendByInstituteAndBatchStatus(Long instituteId, BatchStatus batchStatus, ReportingPeriod period, ReportingPeriodRange range) {
        return switch (period){
            case TODAY -> getHourlyBatchesTrend(instituteId, batchStatus, range);
            case CURRENT_WEEK,CURRENT_MONTH,CURRENT_3_MONTHS -> getDailyBatchesTrend(instituteId, batchStatus, range);
            case CURRENT_YEAR, OVERALL -> getMonthlyBatchesTrend(instituteId, batchStatus, range);
        };
    }

    private List<TrendPoint> getHourlyBatchesTrend(Long instituteId, BatchStatus batchStatus, ReportingPeriodRange range ) {
        List<TrendPointProjection> projections = batchRepository.getHourlyBatchesTrendByInstituteAndBatchStatus(instituteId, batchStatus.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(), range.currentEnd(), TrendType.HOUR , projections);
    }

    private List<TrendPoint> getDailyBatchesTrend(Long instituteId, BatchStatus batchStatus, ReportingPeriodRange range ) {
        List<TrendPointProjection> projections = batchRepository.getDailyBatchesTrendByInstituteAndBatchStatus(instituteId, batchStatus.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(), range.currentEnd(), TrendType.DAY , projections);
    }

    private List<TrendPoint> getMonthlyBatchesTrend(Long instituteId, BatchStatus batchStatus, ReportingPeriodRange range ) {
        List<TrendPointProjection> projections = batchRepository.getMonthlyBatchesTrendByInstituteAndBatchStatus(instituteId, batchStatus.name(), range.currentStart(), range.currentEnd());
        return TrendBuilder.build(range.currentStart(), range.currentEnd(), TrendType.MONTH , projections);
    }
}
