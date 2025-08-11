package tz.co.itrust.vfd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VFD Transaction Entity
 * Represents a VFD transaction (purchase, sale, transfer, dividend)
 */
@Entity
@Table(name = "vfd_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", unique = true, nullable = false, length = 50)
    private String transactionId;

    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    private String currency = "TZS";

    @Column(name = "instrument_code", length = 20)
    private String instrumentCode;

    @Column(name = "instrument_name", length = 100)
    private String instrumentName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price_per_unit", precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "settlement_date")
    private LocalDateTime settlementDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "vfd_reference", length = 100)
    private String vfdReference;

    @Column(name = "external_reference", length = 100)
    private String externalReference;

    @Column(name = "broker_code", length = 20)
    private String brokerCode;

    @Column(name = "broker_name", length = 100)
    private String brokerName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum TransactionType {
        PURCHASE, SALE, TRANSFER, DIVIDEND
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, CANCELLED
    }
} 