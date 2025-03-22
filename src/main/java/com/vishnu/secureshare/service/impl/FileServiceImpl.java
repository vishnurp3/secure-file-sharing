package com.vishnu.secureshare.service.impl;

import com.vishnu.secureshare.dto.FileDownloadResponse;
import com.vishnu.secureshare.dto.FileUploadResponse;
import com.vishnu.secureshare.entity.AuditLog;
import com.vishnu.secureshare.entity.DownloadLink;
import com.vishnu.secureshare.entity.FileMetadata;
import com.vishnu.secureshare.entity.User;
import com.vishnu.secureshare.exception.FileStorageException;
import com.vishnu.secureshare.repository.AuditLogRepository;
import com.vishnu.secureshare.repository.DownloadLinkRepository;
import com.vishnu.secureshare.repository.FileMetadataRepository;
import com.vishnu.secureshare.repository.UserRepository;
import com.vishnu.secureshare.service.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileMetadataRepository fileRepository;
    private final DownloadLinkRepository downloadLinkRepository;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;
    @Value("${file.link.expiry-minutes}")
    private int linkExpiryMinutes;

    @Override
    @Transactional
    public FileUploadResponse uploadFile(String username, MultipartFile file) {
        try {
            User user = Optional.ofNullable(userRepository.findByUsername(username))
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
            Files.createDirectories(Path.of(uploadDir));
            String originalFilename = Path.of(Objects.requireNonNull(file.getOriginalFilename())).getFileName().toString();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;
            Path destinationPath = Path.of(uploadDir).resolve(storedFilename);
            Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            FileMetadata fileMeta = FileMetadata.builder()
                    .owner(user)
                    .filename(originalFilename)
                    .storedPath(destinationPath.toString())
                    .uploadTime(LocalDateTime.now())
                    .fileSize(file.getSize())
                    .build();
            fileRepository.save(fileMeta);
            String token = UUID.randomUUID().toString();
            DownloadLink link = DownloadLink.builder()
                    .file(fileMeta)
                    .uniqueToken(token)
                    .createdAt(LocalDateTime.now())
                    .expiryTime(LocalDateTime.now().plusMinutes(linkExpiryMinutes))
                    .build();
            downloadLinkRepository.save(link);
            auditLogRepository.save(AuditLog.builder()
                    .user(user)
                    .actionType("UPLOAD")
                    .description("Uploaded file: " + originalFilename)
                    .timestamp(LocalDateTime.now())
                    .build());
            return new FileUploadResponse(fileMeta.getId(), fileMeta.getFilename(), link.getDownloadLink());
        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new FileStorageException("File upload failed", e);
        }
    }

    @Override
    public FileDownloadResponse downloadFile(String token) {
        DownloadLink link = getDownloadLinkByToken(token);
        try {
            byte[] data = Files.readAllBytes(Path.of(link.getFile().getStoredPath()));
            auditLogRepository.save(AuditLog.builder()
                    .user(link.getFile().getOwner())
                    .actionType("DOWNLOAD")
                    .description("Downloaded file: " + link.getFile().getFilename())
                    .timestamp(LocalDateTime.now())
                    .build());
            return new FileDownloadResponse(link.getFile().getFilename(), data);
        } catch (IOException e) {
            log.error("File read failed", e);
            throw new FileStorageException("Unable to read file from path: " + link.getFile().getStoredPath(), e);
        }
    }

    private DownloadLink getDownloadLinkByToken(String token) {
        DownloadLink link = downloadLinkRepository.findByUniqueToken(token);
        if (link == null || link.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired download link");
        }
        return link;
    }
}