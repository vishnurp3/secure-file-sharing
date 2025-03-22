package com.vishnu.secureshare.repository;

import com.vishnu.secureshare.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByUserUsernameOrderByTimestampDesc(String username);

}