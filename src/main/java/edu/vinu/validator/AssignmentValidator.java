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

import edu.vinu.entity.AssignmentEntity;
import edu.vinu.common.exception.custom.InvalidInputException;
import edu.vinu.request.assignments.AssignmentUpdateRequest;
import edu.vinu.common.dto.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssignmentValidator {
    public static void validateCreate(Integer totalMarks, LocalDateTime availableOn, LocalDateTime dueDate, boolean resubmission, Integer maxAttempts){
        List<FieldError> errors = new ArrayList<>();

        validateTotalMarks(totalMarks, errors);
        validateDateRelationship(availableOn, dueDate, errors);
        validateResubmission(resubmission,maxAttempts,errors);

        if(!errors.isEmpty()){
            throw new InvalidInputException(errors);
        }
    }

    public static void validateUpdate(AssignmentEntity existing, AssignmentUpdateRequest request){
        List<FieldError> errors = new ArrayList<>();

        validateTotalMarks(request.totalMarks(), errors);
        validateResubmission(request.resubmission(), request.maxAttempts(), errors);

        boolean dateChanged = !Objects.equals(existing.getAvailableOn(), request.availableOn()) || !Objects.equals(existing.getDueDate(), request.dueDate());

        if(dateChanged){
            validateDateRelationship(request.availableOn(), request.dueDate(), errors);
            validateAvailableOnFuture(request.availableOn(), errors);
            validateDueDateFuture(request.dueDate(), errors);
        }

        if(!errors.isEmpty()){
            throw new InvalidInputException(errors);
        }
    }

    private static void validateTotalMarks(Integer totalMarks, List<FieldError> errors) {
        if (totalMarks <= 0) {
            errors.add(new FieldError("totalMarks", "totalMarks must be greater than zero"));
        }
    }

    private static void validateDateRelationship(LocalDateTime availableOn, LocalDateTime dueDate, List<FieldError> errors) {
        if(dueDate!=null && availableOn!=null && !dueDate.isAfter(availableOn)){
            errors.add(new FieldError("dueDate", "Assignment due date must be after available on date!"));
        }
    }

    private static void validateResubmission(boolean resubmission, Integer maxAttempts, List<FieldError> errors) {
        if(resubmission && (maxAttempts==null || maxAttempts <= 1)){
            errors.add(new FieldError("maxAttempts", "Max attempts must be greater than 1 if resubmission is allowed!"));
        }

        if(!resubmission && maxAttempts!=1){
            errors.add(new FieldError("maxAttempts", "Max attempts need to be 1 if resubmission is not allowed!"));
        }
    }

    private static void validateAvailableOnFuture(LocalDateTime availableOn, List<FieldError> errors){
        if(availableOn!=null && availableOn.isBefore(LocalDateTime.now())){
            errors.add(new FieldError("availableOn", "Available on must be future date!"));
        }
    }

    private static void validateDueDateFuture(LocalDateTime dueDate, List<FieldError> errors){
        if(dueDate!=null && dueDate.isBefore(LocalDateTime.now())){
            errors.add(new FieldError("dueDate", "Due date must be a future date!"));
        }
    }
}
