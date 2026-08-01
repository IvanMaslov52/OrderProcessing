package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUsernameRequest {
    @NotBlank
    private String username;
}
