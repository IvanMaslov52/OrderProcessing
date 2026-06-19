package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private Long id;

    private String email;

    private String name;

    private String keycloakId;
}
