package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for VFD settlement responses
 */
@Data
public class VfdSettlementResponse {
    private String transactionId;
    private String settlementId;
    private String status;
    private LocalDateTime settlementTime;
    private String message;
    private BigDecimal amount;
    private String currency;
    private String customerId;
    private String instrumentCode;
    private String brokerCode;
    private Integer priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
