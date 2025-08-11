package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD audit requests
 */
@Data
public class VfdAuditRequest {
    private String auditId;
    private String entityType;
    private String entityId;
    private String action;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String userId;
    private Map<String, Object> filters;
}
