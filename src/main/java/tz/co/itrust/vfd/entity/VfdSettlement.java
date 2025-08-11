package tz.co.itrust.vfd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VFD Settlement Entity
 * Represents a VFD settlement record
 */
@Entity
@Table(name = "vfd_settlements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settlement_id", unique = true, nullable = false, length = 50)
    private String settlementId;

    @Column(name = "transaction_id", nullable = false, length = 50)
    private String transactionId;

    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;

    @Column(name = "instrument_code", length = 20)
    private String instrumentCode;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", length = 3)
    private String currency = "TZS";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SettlementStatus status = SettlementStatus.PENDING;

    @Column(name = "settlement_time")
    private LocalDateTime settlementTime;

    @Column(name = "priority")
    private Integer priority = 1;

    @Column(name = "message", length = 500)
    private String message;

    @Column(name = "broker_code", length = 20)
    private String brokerCode;

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

    public enum SettlementStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
    }
}
