package com.example.userservice.service.impl;

import com.example.userservice.dto.*;
import com.example.userservice.service.IdGeneratorService;
import com.example.userservice.service.KeycloakService;
import com.example.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakService keycloakService;

    private final IdGeneratorService idGeneratorService;
    @Override
    public UserResponseDto registerUser(RegisterRequest registerRequest) {
        Long numericLongId = idGeneratorService.nextId();
        String keycloakId = keycloakService.createUser(registerRequest.getEmail(),
                registerRequest.getName(),
                registerRequest.getPassword(),
                numericLongId);
        return UserResponseDto.builder().
                id(numericLongId).
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
    public UserResponseDto updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        keycloakService.changePassword(updatePasswordRequest.getKeycloakId(), updatePasswordRequest.getPassword());
        return keycloakService.getUserById(updatePasswordRequest.getKeycloakId());
    }

    @Override
    public UserResponseDto updateUsername(UpdateUsernameRequest updateUsernameRequest) {
        keycloakService.updateUsername(updateUsernameRequest.getKeycloakId(), updateUsernameRequest.getUsername());
        return keycloakService.getUserById(updateUsernameRequest.getKeycloakId());
    }
}
