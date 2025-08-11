package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD reconciliation requests
 */
@Data
public class VfdReconciliationRequest {
    private String reconciliationId;
    private String reconciliationType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String entityType;
    private String entityId;
    private Map<String, Object> parameters;
    private String initiatedBy;
    private String description;
    private String breakId;
}
