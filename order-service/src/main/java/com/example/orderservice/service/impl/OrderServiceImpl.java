package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.KafkaMessageProducer;
import com.example.orderservice.service.OrderService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final KafkaMessageProducer producer;

    private final OrderRepository orderRepository;

    @Override
    public void saveOrder(OrderDto orderDto) {
        Order newOrder = new Order(orderDto);
        Order savedOrder = orderRepository.save(newOrder);
        System.out.println("Сохраненный заказ = " + savedOrder);
        producer.sendMessage("default-topic", String.valueOf(savedOrder.getUserId()), savedOrder);
    }

    @Override
    public OrderEvent orderToEvent(Order order) {
        return new OrderEvent(order);
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
