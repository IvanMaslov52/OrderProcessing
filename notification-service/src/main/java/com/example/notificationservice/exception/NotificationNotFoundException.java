package com.example.notificationservice.exception;

import org.springframework.http.HttpStatus;

import static com.example.notificationservice.constants.AppConstants.NOTIFICATION_NOT_FOUND;

public class NotificationNotFoundException extends ApiException {

    public NotificationNotFoundException(String id) {
        super(NOTIFICATION_NOT_FOUND, "Уведомление с подобным id: '" + id + "' не найден", HttpStatus.NOT_FOUND);
    }
}
