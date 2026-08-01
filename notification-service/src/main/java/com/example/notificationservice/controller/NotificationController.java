package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationCount;
import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.dto.NotificationResponse;
import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    /*
     * Тестовый endpoint для просмотра конкретного Order
     * */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotification(@PathVariable("id") String id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotificationByUser(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationByUserId(userId));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<NotificationCount> getUnreadCountNotificationByUser(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationCountByUserId(userId));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> readNotification(@PathVariable("notificationId") String id) {
        return ResponseEntity.ok(notificationService.readNotificationById(id));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<NotificationCount> getNotificationByUser(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(notificationService.readAllNotificationByUserId(userId));
    }
}
