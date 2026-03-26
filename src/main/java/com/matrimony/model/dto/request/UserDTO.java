package com.matrimony.model.dto.request;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String username,
        String email,
        Boolean isActive,
        Boolean emailVerified,
        LocalDateTime lastLoginAt
) {}
