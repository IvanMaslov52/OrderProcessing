package com.example.notificationservice.service.impl;

import com.example.notificationservice.dto.NotificationCount;
import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.dto.NotificationEvent;
import com.example.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.exception.InvalidReadException;
import com.example.notificationservice.exception.NotificationNotFoundException;
import com.example.notificationservice.model.EventType;
import com.example.notificationservice.model.OrderNotification;
import com.example.notificationservice.model.OrderStatus;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.service.NotificationMapper;
import com.example.notificationservice.service.NotificationService;
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

    private final NotificationMapper mapper;

    @Override
    public void saveNotification(NotificationEvent notificationEvent) {
        if(notificationRepository.existsByOrderIdAndEventType(notificationEvent.getOrderId(), setEventTypeByStatus(notificationEvent.getStatus()))) {
            log.warn("Дубликат события: orderId:{}", notificationEvent.getOrderId());
            return;
        }
        OrderNotification newOrderNotification = eventToNotification(notificationEvent);
        OrderNotification savedOrderNotification = notificationRepository.save(newOrderNotification);
        log.info("Сохранение  уведомления о заказе OrderNotification={}", savedOrderNotification);
    }

    @Override
    public OrderNotification eventToNotification(NotificationEvent notificationEvent) {
        return OrderNotification.builder()
                .orderId(notificationEvent.getOrderId())
                .userId(notificationEvent.getUserId())
                .title(formattedTitle(notificationEvent.getStatus().name(), notificationEvent.getProduct()))
                .message(formattedMessage(notificationEvent.getStatus().name(), notificationEvent.getProduct(), notificationEvent.getTotalPrice()))
                .eventType(setEventTypeByStatus(notificationEvent.getStatus()))
                .read(false)
                .createdAt(LocalDateTime.now()).build();
    }

    @Override
    public String formattedTitle(String status, String product) {
        return switch (status) {
            case "PENDING" -> "Creating an order for a product:" + product;
            case "CONFIRMED" -> "Confirmed order for a product:" + product;
            case "CANCELLED" -> "Cancelled order for a product:" + product;
            default -> "Unknown status";
        };
    }

    @Override
    public String formattedMessage(String status, String product, BigDecimal totalPrice) {
        return switch (status) {
            case "PENDING" ->
                    "Order for a product:" + product + " on price: " + totalPrice + " with a pending status has been created.";
            case "CONFIRMED" -> "Order for a product:" + product + " on price: " + totalPrice + " has been confirmed.";
            case "CANCELLED" -> "Order for a product:" + product + " on price: " + totalPrice + " has been canceled.";
            default -> "Unknown status";
        };
    }

    @Override
    public NotificationDto getNotificationById(String id) {
        return mapper.notificationToDto(notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id)));
    }

    @Override
    public List<NotificationDto> getNotificationByUserId(String userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(mapper::notificationToDto)
                .toList();
    }

    @Override
    public List<NotificationDto> getUnreadNotificationByUserId(String userId) {
        return notificationRepository.findByUserId(userId).stream()
                .filter(orderNotification -> !orderNotification.getRead())
                .sorted(Comparator.comparing(OrderNotification::getCreatedAt))
                .map(mapper::notificationToDto)
                .toList();
    }

    @Override
    public NotificationCount getUnreadNotificationCountByUserId(String userId) {
        return new NotificationCount(notificationRepository.findByUserId(userId)
                .stream()
                .filter(n-> !n.getRead()).count());
    }

    @Override
    public NotificationResponse readNotificationById(String id) {
        OrderNotification orderNotification = notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException(id));
        if(orderNotification.getRead()) {
            throw new InvalidReadException();
        }
        orderNotification.setRead(true);
        OrderNotification savedNotification = notificationRepository.save(orderNotification);
        return mapper.notificationToResponse(savedNotification);
    }

    @Override
    public NotificationCount readAllNotificationByUserId(String userId) {
        List<OrderNotification> updatedList =
                notificationRepository.findByUserId(userId).stream()
                .filter(n -> !n.getRead())
                .peek(n-> n.setRead(true))
                .sorted(Comparator.comparing(OrderNotification::getCreatedAt))
                .toList();
        return new NotificationCount(((long) notificationRepository.saveAll(updatedList).size()));
    }

    @Override
    public EventType setEventTypeByStatus(OrderStatus status) {
        return switch (status) {
            case CONFIRMED -> EventType.ORDER_CONFIRMED;
            case CANCELLED -> EventType.ORDER_CANCELED;
            default -> EventType.ORDER_CREATED;
        };
    }
}
