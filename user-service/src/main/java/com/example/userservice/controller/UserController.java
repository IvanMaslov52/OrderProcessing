package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDto registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.registerUser(registerRequest);
    }

    @PostMapping("/login")
    public Mono<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authorization(loginRequest);
    }

    @PostMapping("/edit/username")
    public UserResponseDto updateUsername(@Valid @RequestBody UpdateUsernameRequest request) {
        return userService.updateUsername(request);
    }

    @PostMapping("/edit/password")
    public UserResponseDto updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        return userService.updatePassword(request);
    }
}
