package com.example.notificationservice.repository;

import com.example.notificationservice.model.OrderNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<OrderNotification, String> {
    List<OrderNotification> findByUserId(Long userId);
}
