package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD validation requests
 */
@Data
public class VfdValidationRequest {
    private String requestId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String transactionType;
    private LocalDateTime timestamp;
    private Map<String, Object> validationRules;
    private Map<String, Object> metadata;
    private String instrumentCode;
    private String brokerCode;
}
