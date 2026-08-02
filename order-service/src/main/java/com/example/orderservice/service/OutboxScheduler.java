package com.example.orderservice.service;

import com.example.orderservice.model.OutboxEvent;
import com.example.orderservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxScheduler {

    private final ObjectMapper objectMapper;

    private final OutboxEventRepository outboxEventRepository;

    private final KafkaMessageProducer producer;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> events = outboxEventRepository.findBySentFalseOrderByCreatedAt();
        if(events.isEmpty()) {
            return;
        }

        log.info("Processing {} outbox events", events.size());
        for(OutboxEvent event : events) {
            producer.sendMessage(extractUserId(event.getPayload()), event.getPayload());
            event.setSent(true);
            event.setSentAt(LocalDateTime.now());
            outboxEventRepository.save(event);

            log.info("Outbox event was sent id = {}, eventType = {}", event.getOutboxId(), event.getEventType());
        }
    }

    private String extractUserId(String payload) {
        try {
            JsonNode node = objectMapper.readTree(payload);
            return node.get("userId").asText();
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
