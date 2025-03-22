package com.vishnu.secureshare.dto;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}
