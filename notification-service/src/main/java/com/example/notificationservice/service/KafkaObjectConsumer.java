package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-notification")
    public void consume(String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            NotificationEvent notificationEvent = objectMapper.readValue(message, NotificationEvent.class);
            notificationService.saveNotification(notificationEvent);
            log.info("Получено событие из partition={} offset={}: {}", partition, offset, notificationEvent);
        } catch (JsonProcessingException e) {
            log.info("Ошибка при парсинге объект OrderEvent из кафка = {}", e.getMessage());
        }
    }

}
