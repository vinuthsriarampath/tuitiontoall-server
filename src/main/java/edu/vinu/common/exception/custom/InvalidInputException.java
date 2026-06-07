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

package edu.vinu.common.exception.custom;

import edu.vinu.response.FieldError;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InvalidInputException extends RuntimeException {
    private final List<FieldError> errors;

    public InvalidInputException(String message) {
        super(message);
        errors = new ArrayList<>();
    }

    // Single field error
    public InvalidInputException(String field, String message) {
        super(message);
        this.errors = List.of(new FieldError(field, message));
    }

    // Multiple field errors
    public InvalidInputException(List<FieldError> errors) {
        super("Validation failed");
        this.errors = errors;
    }
}
