package com.example.orderservice.dto;

import com.example.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Long orderId;

    private Long userId;

    private String product;

    private Integer quantity;

    private BigDecimal totalPrice;

    private OrderStatus status;

    private LocalDateTime createdAt;

}
