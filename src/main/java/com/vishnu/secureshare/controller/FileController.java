package com.vishnu.secureshare.controller;

import com.vishnu.secureshare.dto.ApiResponse;
import com.vishnu.secureshare.dto.FileUploadResponse;
import com.vishnu.secureshare.service.FileService;
import com.vishnu.secureshare.util.TrackExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.vishnu.secureshare.constants.FileConstants.FILE_API_BASE_ENDPOINT;
import static com.vishnu.secureshare.constants.FileConstants.FILE_DOWNLOAD_ENDPOINT;

@RestController
@RequestMapping(FILE_API_BASE_ENDPOINT)
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @TrackExecutionTime
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(@RequestParam("file") MultipartFile file,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        var response = fileService.uploadFile(userDetails.getUsername(), file);
        return ResponseEntity.ok(new ApiResponse<>(true, "File uploaded successfully", response));
    }

    @GetMapping(FILE_DOWNLOAD_ENDPOINT + "/{token}")
    @TrackExecutionTime
    public ResponseEntity<byte[]> downloadFile(@PathVariable String token) {
        var response = fileService.downloadFile(token);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + response.fileName() + "\"")
                .body(response.fileData());
    }
}