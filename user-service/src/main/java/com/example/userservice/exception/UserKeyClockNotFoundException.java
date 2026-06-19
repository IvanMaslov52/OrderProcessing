package com.example.userservice.exception;

import org.springframework.http.HttpStatus;

import static com.example.userservice.constants.AppConstants.USER_NOT_FOUND;

public class UserKeyClockNotFoundException extends ApiException {

    public UserKeyClockNotFoundException(String id) {
        super(USER_NOT_FOUND, "Пользователя с подобным KeyClockId: '" + id + "' не найден", HttpStatus.NOT_FOUND);
    }
}
