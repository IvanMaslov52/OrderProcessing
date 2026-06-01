package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.model.Order;

public interface OrderService {
    void saveOrder(OrderDto orderDto);

    OrderEvent orderToEvent(Order order);

    Order findOrderById(Long id);
}
