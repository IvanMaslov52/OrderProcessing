package com.example.userservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {

    private String email;

    private String name;

    private String keycloakId;
}
