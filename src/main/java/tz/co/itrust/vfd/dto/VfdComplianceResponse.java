package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for VFD compliance responses
 */
@Data
public class VfdComplianceResponse {
    private String transactionId;
    private boolean isCompliant;
    private List<String> complianceIssues;
    private List<String> complianceWarnings;
    private LocalDateTime complianceCheckTime;
    private String complianceStatus;
    private Map<String, Object> complianceDetails;
    private String message;
}
