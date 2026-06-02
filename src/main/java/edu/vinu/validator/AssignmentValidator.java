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

package edu.vinu.validator;

import edu.vinu.exception.custom.InvalidInputException;
import edu.vinu.response.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssignmentValidator {
    public static void validate(Integer totalMarks, LocalDateTime availableOn, LocalDateTime dueDate, boolean resubmission, Integer maxAttempts){
        List<FieldError> errors = new ArrayList<>();
        if (totalMarks <= 0) {
            errors.add(new FieldError("totalMarks", "totalMarks must be greater than zero"));
        }

        if(dueDate!=null && availableOn!=null && !dueDate.isAfter(availableOn)){
            errors.add(new FieldError("dueDate", "Assignment due date must be after available on date!"));
        }

        if(resubmission && (maxAttempts==null || maxAttempts <= 1)){
            errors.add(new FieldError("maxAttempts", "Max attempts must be greater than 1 if resubmission is allowed!"));
        }

        if(!resubmission && maxAttempts!=1){
            errors.add(new FieldError("maxAttempts", "Max attempts need to be 1 if resubmission is not allowed!"));
        }

        if(!errors.isEmpty()){
            throw new InvalidInputException(errors);
        }
    }
}
