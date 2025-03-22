package com.vishnu.secureshare.service;

import com.vishnu.secureshare.dto.FileDownloadResponse;
import com.vishnu.secureshare.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileUploadResponse uploadFile(String username, MultipartFile file);

    FileDownloadResponse downloadFile(String token);
}