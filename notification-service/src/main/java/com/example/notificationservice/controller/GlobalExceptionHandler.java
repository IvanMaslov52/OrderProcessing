package com.example.notificationservice.controller;

import com.example.notificationservice.constants.AppConstants;
import com.example.notificationservice.dto.ErrorResponse;
import com.example.notificationservice.dto.FieldErrorDetail;
import com.example.notificationservice.dto.ValidationErrorResponse;
import com.example.notificationservice.exception.InvalidReadException;
import com.example.notificationservice.exception.NotificationNotFoundException;
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

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApiException(NotificationNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(InvalidReadException.class)
    public ResponseEntity<ErrorResponse> handleApiException(InvalidReadException ex) {
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
