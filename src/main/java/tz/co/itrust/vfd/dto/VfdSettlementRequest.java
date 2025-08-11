package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO for VFD settlement requests
 */
@Data
public class VfdSettlementRequest {
    private String transactionId;
    private String customerId;
    private String instrumentCode;
    private BigDecimal amount;
    private String currency;
    private String settlementType;
    private String accountNumber;
    private String bankCode;
    private String brokerCode;
}
