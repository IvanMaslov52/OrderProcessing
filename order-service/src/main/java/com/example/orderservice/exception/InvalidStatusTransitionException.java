package com.example.orderservice.exception;

import com.example.orderservice.constants.AppConstants;
import org.springframework.http.HttpStatus;

public class InvalidStatusTransitionException extends ApiException{
    public InvalidStatusTransitionException(String status, Long orderId) {
        super(AppConstants.NOT_ACCEPTABLE, "Недопустимый переход на статус " + status + " для заказа " + orderId,  HttpStatus.NOT_ACCEPTABLE);
    }
}
