package com.example.notificationservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "order_notification")
@Builder
@ToString
public class OrderNotification {
    @Id
    private String id;
    private Long orderId;
    private Long userId;
    private EventType eventType;
    private String title;
    private String message;
    private Boolean read;
    private LocalDateTime createdAt;
}
