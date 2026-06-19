package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Order toEntity(OrderDto orderDto);

    OrderEvent toOrderEvent(Order order);

    OrderDto toDto(Order order);

    OrderResponseDto toResponseDto(Order order);
}
