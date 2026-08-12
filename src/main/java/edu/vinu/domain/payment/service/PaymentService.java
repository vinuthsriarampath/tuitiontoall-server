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

import edu.vinu.domain.course.entity.CourseEntity;
import edu.vinu.domain.institute.entity.InstituteEntity;
import edu.vinu.domain.payment.entity.Payment;
import edu.vinu.domain.user.entity.StudentEntity;

import java.math.BigDecimal;

public interface PaymentService {
    Payment pay(BigDecimal amount, StudentEntity studentEntity, InstituteEntity instituteEntity);
}
