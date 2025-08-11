package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD webhook responses
 */
@Data
public class VfdWebhookResponse {
    private String webhookId;
    private String status;
    private LocalDateTime processedAt;
    private String message;
    private Map<String, Object> result;
    private String errorCode;
    private String errorMessage;
}
