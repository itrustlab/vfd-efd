package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD audit responses
 */
@Data
public class VfdAuditResponse {
    private String auditId;
    private String entityType;
    private String entityId;
    private String action;
    private String userId;
    private LocalDateTime timestamp;
    private String details;
    private Map<String, Object> changes;
    private String ipAddress;
    private String userAgent;
}
