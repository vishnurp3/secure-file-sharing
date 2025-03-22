package com.vishnu.secureshare.dto;

public record UserResponse(
        Long id,
        String username,
        String role
) {
}