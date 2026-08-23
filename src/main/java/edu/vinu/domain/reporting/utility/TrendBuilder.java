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

package edu.vinu.domain.reporting.utility;

import edu.vinu.domain.reporting.enums.TrendType;
import edu.vinu.domain.reporting.projection.TrendPointProjection;
import edu.vinu.domain.reporting.response.TrendPoint;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrendBuilder {

    private static final DateTimeFormatter BUCKET_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<TrendPoint> build(LocalDateTime start, LocalDateTime end, TrendType trendType, List<TrendPointProjection> projections){

        Map<LocalDateTime, BigDecimal> values =
                projections.stream()
                        .collect(Collectors.toMap(
                                projection -> parseBucket(
                                        projection.getBucket()
                                ),
                                TrendPointProjection::getValue,
                                BigDecimal::add
                        ));

        List<TrendPoint> trend = new ArrayList<>();

        LocalDateTime bucket = createStartDateTime(start, trendType);

        while (bucket.isBefore(end)) {

            BigDecimal value =
                    values.getOrDefault(
                            bucket,
                            BigDecimal.ZERO
                    );

            trend.add(
                    TrendPoint.builder()
                            .date(bucket)
                            .value(value)
                            .build()
            );

            bucket = nextBucket(bucket, trendType);
        }

        return trend;
    }

    private static LocalDateTime parseBucket(String bucket) {
        return LocalDateTime.parse(bucket, BUCKET_FORMATTER);
    }

    private static LocalDateTime createStartDateTime(LocalDateTime startDateTime, TrendType trendType) {

        return switch (trendType) {

            case HOUR -> startDateTime
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);

            case DAY -> startDateTime
                    .toLocalDate()
                    .atStartOfDay();

            case MONTH -> startDateTime
                    .withDayOfMonth(1)
                    .toLocalDate()
                    .atStartOfDay();

            case YEAR -> startDateTime
                    .withDayOfYear(1)
                    .toLocalDate()
                    .atStartOfDay();
        };
    }

    private static LocalDateTime nextBucket(LocalDateTime current, TrendType trendType) {

        return switch (trendType) {
            case HOUR -> current.plusHours(1);
            case DAY -> current.plusDays(1);
            case MONTH -> current.plusMonths(1);
            case YEAR -> current.plusYears(1);
        };
    }
}
