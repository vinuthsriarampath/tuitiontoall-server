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

package edu.vinu.domain.reporting.mapper;

import edu.vinu.domain.reporting.projection.TrendPointProjection;
import edu.vinu.domain.reporting.response.TrendPoint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrendPointMapper {
    private static final DateTimeFormatter BUCKET_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static TrendPoint toTrendPoint(TrendPointProjection trendPointProjection) {
        return TrendPoint.builder()
                .date(LocalDateTime.parse(trendPointProjection.getBucket(), BUCKET_FORMATTER))
                .value(trendPointProjection.getValue())
                .build();
    }
}
