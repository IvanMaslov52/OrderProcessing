package com.example.notificationservice.service.impl;

import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.dto.OrderEvent;
import com.example.notificationservice.model.OrderNotification;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.service.NotificationService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void saveNotification(OrderEvent orderEvent) {
        OrderNotification newOrderNotification = eventToNotification(orderEvent);
        OrderNotification savedOrderNotification = notificationRepository.save(newOrderNotification);
        log.info("Сохранение  уведомления о заказе OrderNotification={}", savedOrderNotification);
    }

    @Override
    public OrderNotification eventToNotification(OrderEvent orderEvent) {
        return OrderNotification.builder()
                .orderId(orderEvent.getOrderId())
                .userId(orderEvent.getUserId())
                .title(formattedTitle(orderEvent.getStatus(), orderEvent.getProduct()))
                .message(formattedMessage(orderEvent.getStatus(), orderEvent.getProduct(), orderEvent.getTotalPrice()))
                .read(false)
                .createdAt(LocalDateTime.now()).build();
    }

    @Override
    public String formattedTitle(String status, String product) {
        switch (status){
            case "PENDING":
                return "Creating an order for a product:" + product;
            case "CONFIRMED":
                return "Confirmed order for a product:" + product;
            case "CANCELLED":
                return "Cancelled order for a product:" + product;
            default:
                return "Unknown status";
        }
    }

    @Override
    public String formattedMessage(String status, String product, BigDecimal totalPrice) {
        switch (status){
            case "PENDING":
                return "Order for a product:" + product + " on price: " + totalPrice + " with a pending status has been created.";
            case "CONFIRMED":
                return "Order for a product:" + product + " on price: " + totalPrice + " has been confirmed.";
            case "CANCELLED":
                return "Order for a product:" + product + " on price: " + totalPrice + " has been canceled.";
            default:
                return "Unknown status";
        }
    }

    @Override
    public NotificationDto getNotificationById(String id) {
        return notificationToDto(notificationRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public List<NotificationDto> getNotificationByUserId(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::notificationToDto)
                .toList();
    }

    @Override
    public List<NotificationDto> getUnreadNotificationByUserId(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(OrderNotification::getCreatedAt))
                .map(this::notificationToDto)
                .filter(orderNotification -> !orderNotification.getRead())
                .toList();
    }

    @Override
    public NotificationDto notificationToDto(OrderNotification notification) {
        return NotificationDto.builder()
                .orderId(notification.getOrderId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.getRead())
                .build();
    }
}
