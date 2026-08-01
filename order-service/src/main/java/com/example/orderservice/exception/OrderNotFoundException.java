package com.example.orderservice.exception;

import com.example.orderservice.constants.AppConstants;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends ApiException{

    public OrderNotFoundException(Long orderId) {
        super(AppConstants.ORDER_NOT_FOUND, "Заказ с подобным id: '" + orderId + "' не найден", HttpStatus.NOT_FOUND);
    }
}
