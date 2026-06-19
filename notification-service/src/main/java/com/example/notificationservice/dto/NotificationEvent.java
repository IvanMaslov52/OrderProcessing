package com.example.notificationservice.dto;

import com.example.notificationservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    private Long orderId;
    private Long userId;
    private String product;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
