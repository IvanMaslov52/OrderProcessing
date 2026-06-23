package com.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdGeneratorService {

    private final RedisTemplate<String, Long> redisTemplate;

    private static final String KEY = "sequence:user:id";

    public Long nextId() {
        return redisTemplate.opsForValue().increment(KEY);
    }
}
