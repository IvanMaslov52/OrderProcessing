package com.example.notificationservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    public String id;
    public Long orderId;
    public Long userId;
    private String title;
    private String message;
    private Boolean read;
    private LocalDateTime createdAt;
}
