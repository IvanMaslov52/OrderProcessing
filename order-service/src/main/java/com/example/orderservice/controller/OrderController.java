package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.dto.OrderResponseDto;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.dto.OrderDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestHeader("X-User-Id") Long userId, @Valid @RequestBody OrderDto orderDto) {
       return ResponseEntity.ok(orderService.saveOrder(userId, orderDto));
    }

    /*
    * Тестовый endpoint для просмотра конкретного Order
    * */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEvent> getOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(orderService.findOrdersByUserId(userId));
    }

    @PatchMapping ("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable("orderId") Long orderId, @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId, userId));
    }

    @PatchMapping ("/{orderId}/confirm")
    public ResponseEntity<OrderResponseDto> confirmOrder(@PathVariable("orderId") Long orderId, @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId, userId));
    }
}
