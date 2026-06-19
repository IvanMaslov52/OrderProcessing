package com.example.userservice.exception;

import org.springframework.http.HttpStatus;

import static com.example.userservice.constants.AppConstants.USER_NOT_FOUND;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException(Long id) {
        super(USER_NOT_FOUND, "Пользователя с подобным id: '" + id + "' не найден", HttpStatus.NOT_FOUND);
    }
}
