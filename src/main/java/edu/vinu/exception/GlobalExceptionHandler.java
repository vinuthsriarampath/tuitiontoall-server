/*
 * Copyright (c) 2025 vinuth sri arampath
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

package edu.vinu.exception;

import edu.vinu.exception.custom.*;
import edu.vinu.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ApiResponse> handleInvalidInputException(InvalidInputException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleJsonParseException(HttpMessageNotReadableException ex) {
        ApiResponse response = new ApiResponse("Invalid JSON input: " +ex.getMessage(),null);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse> handleDateTimeParseException(DateTimeParseException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(),null);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiResponse> handleInternalServerErrorException(InternalServerErrorException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex){
        ApiResponse response = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
