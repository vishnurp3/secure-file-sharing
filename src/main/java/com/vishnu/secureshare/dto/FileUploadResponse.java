package com.vishnu.secureshare.dto;

public record FileUploadResponse(
        Long fileId,
        String fileName,
        String downloadLink
) {
}