package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD external API requests
 */
@Data
public class VfdExternalApiRequest {
    private String transactionId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String transactionType;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
    private String vfdReference;
}
