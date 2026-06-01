package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.dto.OrderEvent;
import com.example.notificationservice.model.OrderNotification;

import java.math.BigDecimal;
import java.util.List;

public interface NotificationService {
    void saveNotification(OrderEvent orderEvent);

    OrderNotification eventToNotification(OrderEvent orderEvent);

    String formattedTitle(String status, String product);

    String formattedMessage(String status, String product, BigDecimal totalPrice);

    NotificationDto getNotificationById(String id);

    List<NotificationDto> getNotificationByUserId(Long userId);

    List<NotificationDto> getUnreadNotificationByUserId(Long userId);

    NotificationDto notificationToDto(OrderNotification notification);
}
