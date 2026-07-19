package com.example.orderservice.dto;

import com.example.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long orderId;

    private String userId;

    private String product;

    private Integer quantity;

    private BigDecimal totalPrice;

    private OrderStatus status;
}
