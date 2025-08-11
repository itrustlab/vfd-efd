package tz.co.itrust.vfd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tz.co.itrust.vfd.entity.VfdCustomerProfile;

import java.util.List;
import java.util.Optional;

/**
 * VFD Customer Profile Repository
 * Handles database operations for VFD customer profiles
 */
@Repository
public interface VfdCustomerProfileRepository extends JpaRepository<VfdCustomerProfile, Long> {

    /**
     * Find customer profile by customer ID
     */
    Optional<VfdCustomerProfile> findByCustomerId(String customerId);

    /**
     * Find customer profile by VFD account number
     */
    Optional<VfdCustomerProfile> findByVfdAccountNumber(String vfdAccountNumber);

    /**
     * Find customer profile by ID number
     */
    Optional<VfdCustomerProfile> findByIdNumber(String idNumber);

    /**
     * Find customer profiles by account status
     */
    List<VfdCustomerProfile> findByAccountStatus(VfdCustomerProfile.AccountStatus accountStatus);

    /**
     * Find customer profiles by KYC status
     */
    List<VfdCustomerProfile> findByKycStatus(VfdCustomerProfile.KycStatus kycStatus);

    /**
     * Find customer profiles by customer type
     */
    List<VfdCustomerProfile> findByCustomerType(VfdCustomerProfile.CustomerType customerType);

    /**
     * Find customer profiles by risk profile
     */
    List<VfdCustomerProfile> findByRiskProfile(VfdCustomerProfile.RiskProfile riskProfile);

    /**
     * Check if customer exists by customer ID
     */
    boolean existsByCustomerId(String customerId);

    /**
     * Check if VFD account number exists
     */
    boolean existsByVfdAccountNumber(String vfdAccountNumber);

    /**
     * Check if ID number exists
     */
    boolean existsByIdNumber(String idNumber);
} 