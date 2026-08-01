package com.example.notificationservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "order_notification")
@CompoundIndexes({
        @CompoundIndex(name = "order_event_unique", def = "{'orderId': 1, 'eventType': 1}", unique = true)
})
@Builder
@ToString
public class OrderNotification {
    @Id
    private String id;
    private Long orderId;
    private String userId;
    private EventType eventType;
    private String title;
    private String message;
    private Boolean read;
    private LocalDateTime createdAt;
}
