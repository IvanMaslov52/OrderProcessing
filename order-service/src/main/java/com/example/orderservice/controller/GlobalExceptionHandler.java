package com.example.orderservice.controller;

import com.example.orderservice.constants.AppConstants;
import com.example.orderservice.dto.ErrorResponse;
import com.example.orderservice.dto.FieldErrorDetail;
import com.example.orderservice.dto.ValidationErrorResponse;
import com.example.orderservice.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleApiException(MethodArgumentTypeMismatchException ex) {
        ErrorResponse error = new ErrorResponse(
                AppConstants.BAD_REQUEST,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApiException(OrderNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }
    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleApiException(InvalidStatusTransitionException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldErrorDetail(
                        fe.getField(),
                        fe.getRejectedValue(),
                        fe.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        ValidationErrorResponse error = new ValidationErrorResponse(
                AppConstants.VALIDATION_FAILED,
                "Ошибка валидации входных данных",
                LocalDateTime.now(),
                fieldErrors
        );
        return ResponseEntity.badRequest().body(error);
    }
}
