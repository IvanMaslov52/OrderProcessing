package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();

    UserResponseDto findById(Long userId);

    UserResponseDto findByKeyCloakId(String keyClockId);

    UserResponseDto create(UserDto dto);
}
