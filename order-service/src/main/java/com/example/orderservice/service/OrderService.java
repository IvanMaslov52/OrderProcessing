package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto saveOrder(Long userId, OrderDto orderDto);

    OrderEvent findOrderById(Long id);

    List<OrderResponseDto> findOrdersByUserId(Long userid);

    OrderResponseDto confirmOrder(Long id, Long userId);

    OrderResponseDto cancelOrder(Long id, Long userId);
}
