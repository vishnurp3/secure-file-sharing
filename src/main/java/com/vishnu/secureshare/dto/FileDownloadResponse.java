package com.vishnu.secureshare.dto;

public record FileDownloadResponse(
        String fileName,
        byte[] fileData
) {
}