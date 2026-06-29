package com.example.userservice.exception;

import org.springframework.http.HttpStatus;

import static com.example.userservice.constants.AppConstants.INVALID_CREDENTIALS;

public class InvalidCredentialsException extends ApiException {

    public InvalidCredentialsException() {
        super(INVALID_CREDENTIALS, "Неверные учетные данные пользователя", HttpStatus.UNAUTHORIZED);
    }
}
