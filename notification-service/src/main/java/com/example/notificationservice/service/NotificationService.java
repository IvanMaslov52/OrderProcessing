package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificationCount;
import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.dto.NotificationEvent;
import com.example.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.model.EventType;
import com.example.notificationservice.model.OrderNotification;
import com.example.notificationservice.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public interface NotificationService {
    void saveNotification(NotificationEvent notificationEvent);

    OrderNotification eventToNotification(NotificationEvent notificationEvent);

    String formattedTitle(String status, String product);

    String formattedMessage(String status, String product, BigDecimal totalPrice);

    NotificationDto getNotificationById(String id);

    List<NotificationDto> getNotificationByUserId(Long userId);

    List<NotificationDto> getUnreadNotificationByUserId(Long userId);

    NotificationCount getUnreadNotificationCountByUserId(Long userId);

    NotificationResponse readNotificationById(String id);

    NotificationCount readAllNotificationByUserId(Long userId);

    EventType setEventTypeByStatus(OrderStatus status);
}
