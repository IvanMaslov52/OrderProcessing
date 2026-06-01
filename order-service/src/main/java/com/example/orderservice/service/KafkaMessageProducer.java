package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageProducer.class);
    private final KafkaTemplate<String, Order> kafkaTemplate;

    public void sendMessage(String topic, String key, Order order) {
        kafkaTemplate.send(topic, key, order)
                .whenComplete((result, ex) -> {
                    if(ex == null) {
                        log.info("Сообщения отправлено в [{}] partition={}, offset={}",
                                topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    } else {
                        log.error("Ошибка отправки сообщения в [{}]: {}", topic, ex.getMessage(), ex);
                    }
                });
    }
}
