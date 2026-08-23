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

import edu.vinu.common.enums.ChangeValueType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class DifferenceCalculator {
    public static BigDecimal calculate(BigDecimal current, BigDecimal previous, ChangeValueType type) {


        if (type == ChangeValueType.ABSOLUTE) {
            return current.subtract(previous);
        }

        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        return current
                .subtract(previous)
                .divide(
                        previous,
                        2,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100));
    }
}
