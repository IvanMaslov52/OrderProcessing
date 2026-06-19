package com.example.orderservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ValidationErrorResponse extends ErrorResponse {

    private List<FieldErrorDetail> errors;
    public ValidationErrorResponse(String code, String message, LocalDateTime timestamp, List<FieldErrorDetail> errors ) {
        super(code, message, timestamp);
        this.errors = errors;
    }
}
