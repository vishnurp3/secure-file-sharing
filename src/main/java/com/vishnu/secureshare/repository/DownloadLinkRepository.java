package com.vishnu.secureshare.repository;

import com.vishnu.secureshare.entity.DownloadLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DownloadLinkRepository extends JpaRepository<DownloadLink, Long> {
    DownloadLink findByUniqueToken(String token);

    List<DownloadLink> findAllByExpiryTimeBefore(LocalDateTime time);
}