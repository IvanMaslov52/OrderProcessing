package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto saveOrder(String userId, OrderDto orderDto);

    OrderEvent findOrderById(Long id);

    List<OrderResponseDto> findOrdersByUserId(String userid);

    OrderResponseDto confirmOrder(Long id, String userId);

    OrderResponseDto cancelOrder(Long id, String userId);
}
