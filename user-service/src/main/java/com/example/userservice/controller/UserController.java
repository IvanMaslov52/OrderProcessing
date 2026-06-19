package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.service.UserService;
import com.example.userservice.service.impl.KeycloakServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private final KeycloakServiceImpl keycloakService;


    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return keycloakService.createUser(registerRequest.getEmail(), registerRequest.getName(), registerRequest.getPassword());
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return keycloakService.getToken(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
