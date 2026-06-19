package com.example.notificationservice.exception;

import org.springframework.http.HttpStatus;

import static com.example.notificationservice.constants.AppConstants.NOT_ACCEPTABLE;

public class InvalidReadException extends ApiException{

    public InvalidReadException() {
        super(NOT_ACCEPTABLE, "Уведомление уже имеет статус прочитано", HttpStatus.NOT_FOUND);
    }
}
