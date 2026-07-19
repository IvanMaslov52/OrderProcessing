package com.example.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class Fallback {
    @RequestMapping("/orders")
    public Mono<ResponseEntity<String>> orderFallback() {
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Сервер заказов сейчас недоступен.Попробуйте позже"));
    }

    @RequestMapping("/notification")
    public Mono<ResponseEntity<String>> notificationFallback() {
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Сервер уведомлений сейчас недоступен.Попробуйте позже"));
    }

    @RequestMapping("/users")
    public Mono<ResponseEntity<String>> userFallback() {
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Сервер авторизации сейчас недоступен.Попробуйте позже"));
    }
}
