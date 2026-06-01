package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationDto;
import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("notification/{id}")
    public ResponseEntity<NotificationDto> getNotification(@PathVariable("id") String id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("notification/getAll/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotificationByUser(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(notificationService.getNotificationByUserId(id));
    }

    @GetMapping("notification/getUnread/{userId}")
    public ResponseEntity<List<NotificationDto>> getUnreadNotificationByUser(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationByUserId(id));
    }
}
