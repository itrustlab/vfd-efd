package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD webhook requests
 */
@Data
public class VfdWebhookRequest {
    private String webhookId;
    private String transactionId;
    private String eventType;
    private LocalDateTime timestamp;
    private BigDecimal amount;
    private String currency;
    private String status;
    private Map<String, Object> payload;
    private String signature;
    private Map<String, Object> metadata;
    private String settlementId;
    private String alertId;
    private String instrumentCode;
    private String regulatoryId;
}
