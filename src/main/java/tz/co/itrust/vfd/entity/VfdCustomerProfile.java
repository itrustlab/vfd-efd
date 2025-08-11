package tz.co.itrust.vfd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * VFD Customer Profile Entity
 * Represents a VFD customer profile with KYC information
 */
@Entity
@Table(name = "vfd_customer_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdCustomerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", unique = true, nullable = false, length = 50)
    private String customerId;

    @Column(name = "vfd_account_number", unique = true, length = 50)
    private String vfdAccountNumber;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, length = 20)
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_type", length = 20)
    private IdType idType;

    @Column(name = "id_number", length = 50)
    private String idNumber;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "country", length = 50)
    private String country = "TANZANIA";

    @Column(name = "tax_id", length = 50)
    private String taxId;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_profile", length = 20)
    private RiskProfile riskProfile = RiskProfile.MODERATE;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", length = 20)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

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

    public enum CustomerType {
        INDIVIDUAL, CORPORATE, INSTITUTIONAL
    }

    public enum IdType {
        NIDA, PASSPORT, DRIVING_LICENSE, COMPANY_REG
    }

    public enum RiskProfile {
        LOW, MODERATE, HIGH
    }

    public enum AccountStatus {
        ACTIVE, SUSPENDED, CLOSED
    }

    public enum KycStatus {
        PENDING, APPROVED, REJECTED
    }
} 