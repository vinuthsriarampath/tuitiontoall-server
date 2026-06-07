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

package edu.vinu.common.exception;

import edu.vinu.common.dto.FieldError;
import edu.vinu.common.exception.custom.*;
import edu.vinu.common.response.ApiResponse;
import edu.vinu.common.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

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
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .message(ex.getMessage().isEmpty() ? "Validation Failed!" : ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(ex.getErrors())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleJsonParseException(HttpMessageNotReadableException ex) {
        ApiResponse response = new ApiResponse("Invalid JSON input: " +ex.getMessage(),null);
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse> handleDateTimeParseException(DateTimeParseException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(),null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiResponse> handleInternalServerErrorException(InternalServerErrorException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(NotFoundException ex){
        ApiResponse response = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handleJwtException(JwtException ex) {
        ApiResponse error = new ApiResponse("Authentication Error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ApiResponse error = new ApiResponse("Token Expired", "Your authentication token has expired");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse> handleDisabledException(DisabledException ex) {
        ApiResponse error = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex){
        ApiResponse error = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex) {
        ApiResponse error = new ApiResponse(ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
