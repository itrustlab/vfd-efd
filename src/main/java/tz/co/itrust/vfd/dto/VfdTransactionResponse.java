package tz.co.itrust.vfd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VFD Transaction Response DTO
 * Used for returning VFD transaction information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdTransactionResponse {

    private Long id;
    private String transactionId;
    private String customerId;
    private String transactionType;
    private BigDecimal amount;
    private String currency;
    private String instrumentCode;
    private String instrumentName;
    private Integer quantity;
    private BigDecimal pricePerUnit;
    private LocalDateTime transactionDate;
    private LocalDateTime settlementDate;
    private String status;
    private String vfdReference;
    private String externalReference;
    private String brokerCode;
    private String brokerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
} 