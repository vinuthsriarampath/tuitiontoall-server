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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

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
}
