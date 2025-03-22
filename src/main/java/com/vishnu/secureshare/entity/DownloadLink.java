package com.vishnu.secureshare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.vishnu.secureshare.constants.FileConstants.FILE_API_BASE_ENDPOINT;
import static com.vishnu.secureshare.constants.FileConstants.FILE_DOWNLOAD_ENDPOINT;

@Entity
@Table(name = "download_links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_id", nullable = false)
    private FileMetadata file;
    @Column(name = "unique_token", nullable = false, unique = true, length = 36)
    private String uniqueToken;
    @Column(nullable = false)
    private LocalDateTime expiryTime;
    private LocalDateTime createdAt;

    public String getDownloadLink() {
        return String.join("/", FILE_API_BASE_ENDPOINT, FILE_DOWNLOAD_ENDPOINT, uniqueToken);
    }
}
