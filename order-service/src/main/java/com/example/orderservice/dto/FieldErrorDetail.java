package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorDetail {
    private String field;
    private Object rejectedValue;
    private String message;
}
