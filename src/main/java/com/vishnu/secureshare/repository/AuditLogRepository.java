package com.vishnu.secureshare.repository;

import com.vishnu.secureshare.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}