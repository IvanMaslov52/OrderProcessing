package com.example.orderservice.service.impl;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.exception.InvalidStatusTransitionException;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.KafkaMessageProducer;
import com.example.orderservice.service.OrderMapper;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final KafkaMessageProducer producer;

    private final OrderMapper mapper;

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponseDto saveOrder(String userId, OrderDto orderDto) {
        Order newOrder = mapper.toEntity(orderDto);
        newOrder.setUserId(userId);
        Order savedOrder = orderRepository.save(newOrder);
        log.info("Сохраненный заказ Order={}", savedOrder);
        producer.sendMessage(String.valueOf(savedOrder.getUserId()), mapper.toOrderEvent(savedOrder));
        return mapper.toResponseDto(savedOrder);
    }

    @Override
    public OrderEvent findOrderById(Long id) {
        return mapper.toOrderEvent(orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id)));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "order", key = "#userid")
    public List<OrderResponseDto> findOrdersByUserId(String userid) {
        return orderRepository.findByUserId(userid).stream().map(mapper::toResponseDto).toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "order", key = "#userId")
    public OrderResponseDto confirmOrder(Long id, String userId) {
        Order foundedOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if(!foundedOrder.getStatus().equals(OrderStatus.PENDING)) {
            throw new InvalidStatusTransitionException(OrderStatus.PENDING.name(), id);
        }
        foundedOrder.setStatus(OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(foundedOrder);
        log.info("Сохраненный заказ Order={}", savedOrder);
        producer.sendMessage(String.valueOf(savedOrder.getUserId()), mapper.toOrderEvent(savedOrder));
        return mapper.toResponseDto(savedOrder);
    }

    @Override
    @Transactional
    @CacheEvict(value = "order", key = "#userId")
    public OrderResponseDto cancelOrder(Long id, String userId) {
        Order foundedOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if(!foundedOrder.getStatus().equals(OrderStatus.PENDING)) {
            throw new InvalidStatusTransitionException(OrderStatus.PENDING.name(), id);
        }
        foundedOrder.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(foundedOrder);
        log.info("Сохраненный заказ Order={}", savedOrder);
        producer.sendMessage(String.valueOf(savedOrder.getUserId()), mapper.toOrderEvent(savedOrder));
        return mapper.toResponseDto(savedOrder);
    }
}
