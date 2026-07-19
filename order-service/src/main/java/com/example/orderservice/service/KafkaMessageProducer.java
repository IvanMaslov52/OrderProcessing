package com.example.orderservice.service;

import com.example.orderservice.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {

    @Value("${spring.kafka.topics.orders}")
    private String ordersTopicName;

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageProducer.class);
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendMessage(String key, OrderEvent orderEvent) {
        kafkaTemplate.send(ordersTopicName, key, orderEvent)
                .whenComplete((result, ex) -> {
                    if(ex == null) {
                        log.info("Сообщения отправлено в [{}] partition={}, offset={}",
                                ordersTopicName, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    } else {
                        log.error("Ошибка отправки сообщения в [{}]: {}", ordersTopicName, ex.getMessage(), ex);
                    }
                });
    }
}
