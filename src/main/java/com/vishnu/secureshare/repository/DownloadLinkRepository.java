package com.vishnu.secureshare.repository;

import com.vishnu.secureshare.entity.DownloadLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadLinkRepository extends JpaRepository<DownloadLink, Long> {
    DownloadLink findByUniqueToken(String token);
}