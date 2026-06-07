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

package edu.vinu.domain.schedule_lecture.annotation;

import edu.vinu.domain.schedule_lecture.validator.ScheduleLectureTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // Defines where annotation can be used.
@Retention(RetentionPolicy.RUNTIME) // How long annotation should exist.
@Constraint(validatedBy = ScheduleLectureTimeValidator.class) // Specifies the validator class.
@Documented // Indicates that this annotation should be included in Javadoc.
public @interface ValidScheduleLectureTime {
    String message() default "Invalid Schedule Lecture Time"; // Default validation message (if not provided)
    Class<?>[] groups() default {}; // Advanced feature for grouping constraints, not used in this example.
    Class<? extends Payload>[] payload() default {}; // Used for metadata security levels
}
