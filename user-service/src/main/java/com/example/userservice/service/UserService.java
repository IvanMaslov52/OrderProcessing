package com.example.userservice.service;

import com.example.userservice.dto.*;
import reactor.core.publisher.Mono;


public interface UserService {
    UserResponseDto registerUser(RegisterRequest registerRequest);

    Mono<TokenResponse> authorization(LoginRequest loginRequest);

    UserResponseDto updatePassword(UpdatePasswordRequest updatePasswordRequest);

    UserResponseDto updateUsername(UpdateUsernameRequest updateUsernameRequest);
}
