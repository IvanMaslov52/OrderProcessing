package com.example.userservice.service.impl;

import com.example.userservice.dto.*;
import com.example.userservice.service.KeycloakService;
import com.example.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakService keycloakService;

    @Override
    public UserResponseDto registerUser(RegisterRequest registerRequest) {
        String keycloakId = keycloakService.createUser(registerRequest.getEmail(),
                registerRequest.getName(),
                registerRequest.getPassword());
        return UserResponseDto.builder().
                name(registerRequest.getName()).
                email(registerRequest.getEmail()).
                keycloakId(keycloakId).
                build();
    }

    @Override
    public Mono<TokenResponse> authorization(LoginRequest loginRequest) {
        return keycloakService.getToken(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Override
    public UserResponseDto updatePassword(String userId, UpdatePasswordRequest updatePasswordRequest) {
        keycloakService.changePassword(userId, updatePasswordRequest.getPassword());
        return keycloakService.getUserById(userId);
    }

    @Override
    public UserResponseDto updateUsername(String userId, UpdateUsernameRequest updateUsernameRequest) {
        keycloakService.updateUsername(userId, updateUsernameRequest.getUsername());
        return keycloakService.getUserById(userId);
    }
}
