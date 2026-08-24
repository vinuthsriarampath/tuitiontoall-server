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

package edu.vinu.domain.payment.repository;

import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.payment.enums.PaymentStatus;
import edu.vinu.domain.reporting.projection.TrendPointProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = """
    SELECT COALESCE(SUM(p.amount), 0)
        FROM payment p
            INNER JOIN student_batch_enrollment sbe ON sbe.payment_id = p.id
            INNER JOIN batch b ON sbe.batch_id = b.id
        WHERE b.course_id = :courseId
        AND p.payment_status =:paymentStatus
    """, nativeQuery = true)
    BigDecimal sumRevenueByCourseId(Long courseId, PaymentStatus paymentStatus);

    @Query(value = """
    SELECT COALESCE(SUM(p.amount), 0)
    FROM payment p
    WHERE p.institute_id = :instituteId
    AND p.payment_status =:paymentStatus
    """,nativeQuery = true)
    BigDecimal sumRevenueByInstituteAndStatus(Long instituteId, String paymentStatus);

    @Query(value = """
    SELECT COALESCE(SUM(p.amount), 0)
    FROM payment p
    WHERE p.institute_id = :instituteId
    AND p.payment_status =:paymentStatus
    AND p.created_date >= :startDateTime
    AND p.created_date < :endDateTime
    """,nativeQuery = true)
    BigDecimal sumRevenueByInstituteAndStatusBetween(Long instituteId, String paymentStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    DATE_FORMAT(P.created_date, '%Y-%m-%d %H:00:00') AS bucket,
    COALESCE(SUM(P.amount), 0) AS value
    FROM payment P
    WHERE P.institute_id = :instituteId
    AND P.payment_status = :paymentStatus
    AND P.created_date >= :startDateTime
    AND P.created_date < :endDateTime
    GROUP BY DATE_FORMAT(P.created_date, '%Y-%m-%d %H')
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getHourlyPaymentTrendsByInstituteAndStatus(Long instituteId, String paymentStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    DATE_FORMAT(P.created_date, '%Y-%m-%d 00:00:00') AS bucket,
    COALESCE(SUM(P.amount), 0) AS value
    FROM payment P
    WHERE P.institute_id = :instituteId
    AND P.payment_status = :paymentStatus
    AND P.created_date >= :startDateTime
    AND P.created_date < :endDateTime
    GROUP BY DATE(P.created_date)
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getDailyPaymentTrendsByInstituteAndStatus(Long instituteId, String paymentStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query(value = """
    SELECT
    DATE_FORMAT(P.created_date, '%Y-%m-01 00:00:00') AS bucket,
    COALESCE(SUM(P.amount), 0) AS value
    FROM payment P
    WHERE P.institute_id = :instituteId
    AND P.payment_status = :paymentStatus
    AND P.created_date >= :startDateTime
    AND P.created_date < :endDateTime
    GROUP BY YEAR(P.created_date), MONTH(P.created_date)
    ORDER BY bucket
    """,nativeQuery = true)
    List<TrendPointProjection> getMonthlyPaymentTrendsByInstituteAndStatus(Long instituteId, String paymentStatus, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
