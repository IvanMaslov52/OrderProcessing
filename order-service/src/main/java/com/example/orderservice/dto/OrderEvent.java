package com.example.orderservice.dto;

import com.example.orderservice.model.Order;
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
    public Long orderId;

    public Integer userId;

    public String product;

    public Integer quantity;

    public BigDecimal totalPrice;

    public OrderStatus status;

    public LocalDateTime createdAt;

    public OrderEvent(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUserId();
        this.product = order.getProduct();
        this.quantity = order.getQuantity();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
    }
}
