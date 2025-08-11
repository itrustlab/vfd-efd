package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD external API responses
 */
@Data
public class VfdExternalApiResponse {
    private String transactionId;
    private String vfdReference;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, Object> data;
    private String errorCode;
    private String errorMessage;
}
