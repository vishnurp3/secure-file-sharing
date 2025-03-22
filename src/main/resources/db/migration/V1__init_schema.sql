CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE files
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id    BIGINT       NOT NULL,
    filename    VARCHAR(255) NOT NULL,
    stored_path VARCHAR(512) NOT NULL,
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file_size   BIGINT,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE download_links
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_id      BIGINT    NOT NULL,
    unique_token CHAR(36)  NOT NULL UNIQUE,
    expiry_time  TIMESTAMP NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);

CREATE TABLE audit_logs
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT,
    action_type VARCHAR(50) NOT NULL,
    description TEXT,
    timestamp   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE INDEX idx_files_owner ON files (owner_id);
CREATE INDEX idx_links_token ON download_links (unique_token);
CREATE INDEX idx_audit_user ON audit_logs (user_id);
