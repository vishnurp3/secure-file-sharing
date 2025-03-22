package com.vishnu.secureshare.job;

import com.vishnu.secureshare.repository.DownloadLinkRepository;
import com.vishnu.secureshare.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiredFileCleanupJob {

    private final DownloadLinkRepository downloadLinkRepository;
    private final FileMetadataRepository fileMetadataRepository;

    @Scheduled(cron = "0 0 * * * *") // Runs every hour
    public void deleteExpiredFiles() {
        var expiredLinks = downloadLinkRepository.findAllByExpiryTimeBefore(LocalDateTime.now());
        for (var link : expiredLinks) {
            var file = link.getFile();
            try {
                Files.deleteIfExists(Path.of(file.getStoredPath()));
                log.info("Deleted expired file: {}", file.getStoredPath());
            } catch (Exception e) {
                log.warn("Failed to delete file: {}", file.getStoredPath(), e);
            }
            downloadLinkRepository.delete(link);
            fileMetadataRepository.delete(file);
        }
        log.info("Expired file cleanup complete. Total deleted: {}", expiredLinks.size());
    }
}
