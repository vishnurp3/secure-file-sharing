package com.vishnu.secureshare.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        @NotNull MultipartFile file
) {
}