package com.example.notificationservice.service;

import com.example.notificationservice.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaObjectConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "default-topic", groupId = "consumer-notification-group")
    public void consume(OrderEvent orderEvent, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset) {
        notificationService.saveNotification(orderEvent);
        log.info("Получено событие из partition={}, offset={}: {}", partition, offset, orderEvent);
    }

}
