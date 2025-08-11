package tz.co.itrust.vfd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VFD Transaction Request DTO
 * Used for creating new VFD transactions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdTransactionRequest {

    @NotBlank(message = "Customer ID is required")
    @Size(max = 50, message = "Customer ID must not exceed 50 characters")
    private String customerId;

    @NotNull(message = "Transaction type is required")
    private String transactionType; // PURCHASE, SALE, TRANSFER, DIVIDEND

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Amount must have at most 15 digits and 2 decimal places")
    private BigDecimal amount;

    @Size(max = 3, message = "Currency must not exceed 3 characters")
    private String currency = "TZS";

    @Size(max = 20, message = "Instrument code must not exceed 20 characters")
    private String instrumentCode;

    @Size(max = 100, message = "Instrument name must not exceed 100 characters")
    private String instrumentName;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @DecimalMin(value = "0.01", message = "Price per unit must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price per unit must have at most 10 digits and 2 decimal places")
    private BigDecimal pricePerUnit;

    @NotNull(message = "Transaction date is required")
    private LocalDateTime transactionDate;

    private LocalDateTime settlementDate;

    @Size(max = 20, message = "Broker code must not exceed 20 characters")
    private String brokerCode;

    @Size(max = 100, message = "Broker name must not exceed 100 characters")
    private String brokerName;

    @Size(max = 100, message = "External reference must not exceed 100 characters")
    private String externalReference;

    @Size(max = 50, message = "Created by must not exceed 50 characters")
    private String createdBy;
} 