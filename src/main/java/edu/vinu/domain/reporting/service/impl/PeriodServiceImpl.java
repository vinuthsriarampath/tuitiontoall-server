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

package edu.vinu.domain.reporting.service.impl;

import edu.vinu.domain.reporting.enums.ReportingPeriod;
import edu.vinu.domain.reporting.response.ReportingPeriodRange;
import edu.vinu.domain.reporting.service.PeriodService;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

@Service
public class PeriodServiceImpl implements PeriodService {

    private final Clock clock = Clock.system(ZoneId.of("Asia/Colombo"));

    @Override
    public ReportingPeriodRange getRange(ReportingPeriod period) {
        LocalDate today = LocalDate.now(clock);
        return switch (period) {
            case TODAY -> getTodayRange(today);
            case CURRENT_WEEK -> getCurrentWeekRange(today);
            case CURRENT_MONTH -> getCurrentMonthRange(today);
            case CURRENT_3_MONTHS -> getCurrent3MonthsRange(today);
            case CURRENT_YEAR -> getCurrentYearRange(today);
            case OVERALL -> throw new UnsupportedOperationException("OVERALL reporting period is not supported yet.");
        };
    }

    @Override
    public ReportingPeriodRange getRange(ReportingPeriod period, LocalDate instituteCreatedAt){
        if(period == ReportingPeriod.OVERALL){
            LocalDate today = LocalDate.now(clock);
            LocalDateTime start = instituteCreatedAt.withDayOfYear(1).atStartOfDay();
            LocalDateTime end = today.plusMonths(1).withDayOfMonth(1).atStartOfDay();

            return new ReportingPeriodRange(start, end, null, null);
        } else {
            return getRange(period);
        }
    }

    private ReportingPeriodRange getTodayRange(LocalDate today) {
        LocalDateTime currentStart = today.atStartOfDay();
        LocalDateTime currentEnd = today.plusDays(1).atStartOfDay();

        LocalDateTime previousStart = today.minusDays(1).atStartOfDay();
        LocalDateTime previousEnd = today.atStartOfDay();

        return  new ReportingPeriodRange(currentStart, currentEnd, previousStart, previousEnd);
    }

    private ReportingPeriodRange getCurrentWeekRange(LocalDate today) {
        LocalDate currentStartDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)); // returns the date of the nearest Monday on or before today
        LocalDateTime currentStart = currentStartDate.atStartOfDay();
        LocalDateTime currentEnd = currentStartDate.plusWeeks(1).atStartOfDay();

        LocalDateTime previousStart = currentStartDate.minusWeeks(1).atStartOfDay();
        LocalDateTime previousEnd = currentStartDate.atStartOfDay();

        return new ReportingPeriodRange(currentStart, currentEnd, previousStart, previousEnd);
    }

    private ReportingPeriodRange getCurrentMonthRange(LocalDate today) {
        LocalDate currentStartDate = today.withDayOfMonth(1);

        LocalDateTime currentStart = currentStartDate.atStartOfDay();
        LocalDateTime currentEnd = currentStartDate.plusMonths(1).atStartOfDay();

        LocalDateTime previousStart = currentStartDate.minusMonths(1).atStartOfDay();
        LocalDateTime previousEnd = currentStartDate.atStartOfDay();

        return new ReportingPeriodRange(currentStart, currentEnd, previousStart, previousEnd);

    }

    private ReportingPeriodRange getCurrent3MonthsRange(LocalDate today) {
        LocalDate currentStartDate = today.withDayOfMonth(1).minusMonths(2);

        LocalDateTime currentStart = currentStartDate.atStartOfDay();
        LocalDateTime currentEnd = today.plusDays(1).atStartOfDay();

        LocalDateTime previousStart = currentStartDate.minusMonths(3).atStartOfDay();
        LocalDateTime previousEnd = currentStartDate.atStartOfDay();

        return new ReportingPeriodRange(currentStart, currentEnd, previousStart, previousEnd);
    }

    private ReportingPeriodRange getCurrentYearRange(LocalDate today) {
        LocalDate currentStartDate = today.withDayOfYear(1);

        LocalDateTime currentStart = currentStartDate.atStartOfDay();
        LocalDateTime currentEnd = currentStartDate.plusYears(1).atStartOfDay();

        LocalDateTime previousStart = currentStartDate.minusYears(1).atStartOfDay();
        LocalDateTime previousEnd = currentStartDate.atStartOfDay();

        return new ReportingPeriodRange(currentStart, currentEnd, previousStart, previousEnd);
    }
}
