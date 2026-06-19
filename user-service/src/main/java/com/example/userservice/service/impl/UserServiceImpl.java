package com.example.userservice.service.impl;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public List<UserResponseDto> findAll() {
        return null;
    }

    @Override
    public UserResponseDto findById(Long userId) {
        return null;
    }

    @Override
    public UserResponseDto findByKeyCloakId(String keyClockId) {
        return null;
    }

    @Override
    public UserResponseDto create(UserDto dto) {
        return null;
    }
}
