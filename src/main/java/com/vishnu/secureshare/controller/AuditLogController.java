package com.vishnu.secureshare.controller;

import com.vishnu.secureshare.dto.ApiResponse;
import com.vishnu.secureshare.entity.AuditLog;
import com.vishnu.secureshare.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuditLog>>> getMyAuditLogs(@AuthenticationPrincipal UserDetails userDetails) {
        List<AuditLog> logs = auditLogRepository.findAllByUserUsernameOrderByTimestampDesc(userDetails.getUsername());
        return ResponseEntity.ok(new ApiResponse<>(true, "Audit logs fetched", logs));
    }
}
