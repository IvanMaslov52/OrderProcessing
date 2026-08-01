package com.example.userservice.exception;

import org.springframework.http.HttpStatus;

import static com.example.userservice.constants.AppConstants.USER_CONFLICT;

public class UserKeyClockAlreadyExistException extends ApiException {

    public UserKeyClockAlreadyExistException(String username, String email) {
        super(USER_CONFLICT, "Пользователь с подобным username: '" + username + "' и email: '" + email + "' уже существует", HttpStatus.CONFLICT);
    }
}
