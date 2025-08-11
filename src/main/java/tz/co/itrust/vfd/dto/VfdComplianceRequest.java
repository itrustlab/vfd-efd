package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD compliance requests
 */
@Data
public class VfdComplianceRequest {
    private String transactionId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String transactionType;
    private LocalDateTime timestamp;
    private String complianceType;
    private String violationId;
    private Map<String, Object> complianceRules;
    private Map<String, Object> metadata;
}
