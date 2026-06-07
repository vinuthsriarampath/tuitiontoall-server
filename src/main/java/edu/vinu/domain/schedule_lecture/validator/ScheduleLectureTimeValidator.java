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

package edu.vinu.domain.schedule_lecture.validator;

import edu.vinu.annotation.ValidScheduleLectureTime;
import edu.vinu.domain.schedule_lecture.request.create.ScheduleLectureCreateRequest;
import edu.vinu.domain.schedule_lecture.request.update.ScheduleLectureUpdateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// ConstraintValidator is the Jakarta validation interface
// generic 1 -> Which annotation this class handles
// generic 2 -> What type is being validated:
// Field validation   ->	String
// Integer validation ->	Integer
// Class validation	  ->    Object
public class ScheduleLectureTimeValidator implements ConstraintValidator<ValidScheduleLectureTime, Object> {
    // This method is automatically called by validation engine.
    // value -> The actual DTO object.
    // ConstraintValidatorContext -> This allows: Custom error messages, Field-specific errors, Multiple validation errors, Disabling default messages
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        LocalDate startDate = null;
        LocalTime startTime = null;
        LocalTime endTime = null;

        if (value instanceof ScheduleLectureCreateRequest request) {
            startDate = request.startDate();
            startTime = request.startTime();
            endTime = request.endTime();
        }

        if (value instanceof ScheduleLectureUpdateRequest request) {
            startDate = request.startDate();
            startTime = request.startTime();
            endTime = request.endTime();
        }

        if (startDate == null || startTime == null || endTime == null) {
            return true;
        }

        context.disableDefaultConstraintViolation(); // Disable default generic message and this allows to create proper field level messages.

        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(startDate, endTime);

        boolean valid = true;

        // Validate future datetime
        if (startDateTime.isBefore(LocalDateTime.now())) {

            context.buildConstraintViolationWithTemplate("Meeting start date time must be in the future!")
                    .addPropertyNode("startTime")// Associates validation error with specific field.
                    .addConstraintViolation();

            valid = false;
        }

        // Validate end time > start time
        if (!endDateTime.isAfter(startDateTime)) {

            context.buildConstraintViolationWithTemplate("Meeting end time must be after start time!")
                    .addPropertyNode("endTime")// Associates validation error with specific field.
                    .addConstraintViolation();

            valid = false;
        }

        return valid;
    }
}
