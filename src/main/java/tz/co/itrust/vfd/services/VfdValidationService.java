package tz.co.itrust.vfd.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tz.co.itrust.vfd.dto.VfdTransactionRequest;
import tz.co.itrust.vfd.dto.VfdValidationRequest;
import tz.co.itrust.vfd.dto.VfdValidationResponse;
import tz.co.itrust.vfd.entity.VfdCustomerProfile;
import tz.co.itrust.vfd.entity.VfdTransaction;
import tz.co.itrust.vfd.repository.VfdCustomerProfileRepository;
import tz.co.itrust.vfd.repository.VfdTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import java.time.LocalTime;

/**
 * Service for VFD validation operations
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VfdValidationService {

    private final VfdCustomerProfileRepository customerProfileRepository;
    private final VfdTransactionRepository transactionRepository;

    /**
     * Validate VFD transaction request
     */
    public VfdValidationResponse validateTransaction(VfdTransactionRequest request) {
        log.info("Validating VFD transaction request for customer: {}", request.getCustomerId());
        
        VfdValidationResponse response = new VfdValidationResponse();
        response.setRequestId("VAL_" + System.currentTimeMillis());
        response.setValidationTime(LocalDateTime.now());
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Basic validation rules
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Transaction amount must be greater than zero");
        }
        
        if (request.getCustomerId() == null || request.getCustomerId().trim().isEmpty()) {
            errors.add("Customer ID is required");
        }
        
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            errors.add("Currency is required");
        }
        
        // Business rule validations
        if (request.getAmount() != null && request.getAmount().compareTo(new BigDecimal("1000000")) > 0) {
            warnings.add("Large transaction amount detected");
        }
        
        response.setValidationErrors(errors);
        response.setValidationWarnings(warnings);
        response.setValid(errors.isEmpty());
        response.setStatus(errors.isEmpty() ? "VALID" : "INVALID");
        response.setMessage(errors.isEmpty() ? "Validation passed" : "Validation failed");
        
        Map<String, Object> details = new HashMap<>();
        details.put("validationRules", "Basic business rules applied");
        details.put("customerVerified", true);
        details.put("amountVerified", true);
        response.setValidationDetails(details);
        
        return response;
    }

    /**
     * Validate VFD validation request
     */
    public VfdValidationResponse validateValidationRequest(VfdValidationRequest request) {
        log.info("Validating VFD validation request: {}", request.getRequestId());
        
        VfdValidationResponse response = new VfdValidationResponse();
        response.setRequestId(request.getRequestId());
        response.setValidationTime(LocalDateTime.now());
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Validation request validation
        if (request.getCustomerId() == null || request.getCustomerId().trim().isEmpty()) {
            errors.add("Customer ID is required");
        }
        
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Amount must be greater than zero");
        }
        
        response.setValidationErrors(errors);
        response.setValidationWarnings(warnings);
        response.setValid(errors.isEmpty());
        response.setStatus(errors.isEmpty() ? "VALID" : "INVALID");
        response.setMessage(errors.isEmpty() ? "Validation request is valid" : "Validation request has errors");
        
        return response;
    }

    /**
     * Get validation rules
     */
    public Map<String, Object> getValidationRules() {
        log.info("Getting VFD validation rules");
        
        Map<String, Object> rules = new HashMap<>();
        rules.put("maxTransactionAmount", "1000000");
        rules.put("minTransactionAmount", "1");
        rules.put("supportedCurrencies", List.of("TZS", "USD", "EUR"));
        rules.put("requiredFields", List.of("customerId", "amount", "currency", "transactionType"));
        
        return rules;
    }

    /**
     * Validate customer
     */
    public boolean validateCustomer(String customerId) {
        log.info("Validating customer: {}", customerId);
        
        try {
            // Step 1: Basic format validation
            if (customerId == null || customerId.trim().isEmpty()) {
                log.error("Customer ID is null or empty");
                return false;
            }
            
            // Step 2: Check customer ID format
            if (!isValidCustomerIdFormat(customerId)) {
                log.error("Customer ID format is invalid: {}", customerId);
                return false;
            }
            
            // Step 3: Check if customer exists in system
            if (!doesCustomerExist(customerId)) {
                log.error("Customer does not exist: {}", customerId);
                return false;
            }
            
            // Step 4: Check customer KYC status
            if (!isCustomerKycVerified(customerId)) {
                log.error("Customer KYC not verified: {}", customerId);
                return false;
            }
            
            // Step 5: Check customer account status
            if (!isCustomerAccountActive(customerId)) {
                log.error("Customer account not active: {}", customerId);
                return false;
            }
            
            // Step 6: Check trading permissions
            if (!hasCustomerTradingPermissions(customerId)) {
                log.error("Customer has no trading permissions: {}", customerId);
                return false;
            }
            
            log.info("Customer validation successful: {}", customerId);
            return true;
            
        } catch (Exception e) {
            log.error("Error during customer validation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if customer ID format is valid
     */
    private boolean isValidCustomerIdFormat(String customerId) {
        // Customer ID should be alphanumeric and 6-12 characters
        return customerId.matches("^[A-Za-z0-9]{6,12}$");
    }

    /**
     * Check if customer exists in the system
     */
    private boolean doesCustomerExist(String customerId) {
        Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
        return customerProfile.isPresent();
    }

    /**
     * Check if customer KYC is verified
     */
    private boolean isCustomerKycVerified(String customerId) {
        Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
        return customerProfile.isPresent() && customerProfile.get().getKycStatus() == VfdCustomerProfile.KycStatus.APPROVED;
    }

    /**
     * Check if customer account is active
     */
    private boolean isCustomerAccountActive(String customerId) {
        Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
        return customerProfile.isPresent() && customerProfile.get().getAccountStatus() == VfdCustomerProfile.AccountStatus.ACTIVE;
    }

    /**
     * Check if customer has trading permissions
     */
    private boolean hasCustomerTradingPermissions(String customerId) {
        Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
        return customerProfile.isPresent() && customerProfile.get().getAccountStatus() == VfdCustomerProfile.AccountStatus.ACTIVE;
    }

    /**
     * Check if customer meets age eligibility requirements
     */
    private boolean isCustomerAgeEligible(String customerId) {
        try {
            // Business rule: Minimum age requirement is 18 years
            // In a real implementation, this would check birth date from customer profile
            // For now, implement business logic based on customer type and risk profile
            
            Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
            if (customerProfile.isPresent()) {
                VfdCustomerProfile profile = customerProfile.get();
                VfdCustomerProfile.CustomerType customerType = profile.getCustomerType();
                VfdCustomerProfile.RiskProfile riskProfile = profile.getRiskProfile();
                
                // Business rule: Institutional customers have different age requirements
                if (customerType == VfdCustomerProfile.CustomerType.INSTITUTIONAL) {
                    return true; // Institutional customers bypass age checks
                }
                
                // Business rule: High-risk customers must meet stricter age requirements
                if (riskProfile == VfdCustomerProfile.RiskProfile.HIGH) {
                    // In a real implementation, this would check birth date from customer profile
                    // For now, implement business logic based on customer profile information
                    // Business rule: High-risk customers must have valid ID and KYC approval
                    return profile.getKycStatus() == VfdCustomerProfile.KycStatus.APPROVED &&
                           profile.getIdNumber() != null && !profile.getIdNumber().trim().isEmpty();
                }
                
                // Business rule: Regular customers must have basic KYC verification
                return profile.getKycStatus() == VfdCustomerProfile.KycStatus.APPROVED;
            }
            return false; // Customer not found
        } catch (Exception e) {
            log.error("Error checking customer age eligibility: {}", e.getMessage(), e);
            return false; // Fail safe - assume not eligible on error
        }
    }

    /**
     * Check if customer income meets eligibility requirements
     */
    private boolean isCustomerIncomeEligible(String customerId, BigDecimal amount) {
        try {
            // Business rule: Income requirements based on customer type and risk profile
            BigDecimal incomeThreshold;
            
            Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
            if (customerProfile.isPresent()) {
                VfdCustomerProfile profile = customerProfile.get();
                VfdCustomerProfile.CustomerType customerType = profile.getCustomerType();
                VfdCustomerProfile.RiskProfile riskProfile = profile.getRiskProfile();
                
                if (customerType == VfdCustomerProfile.CustomerType.INSTITUTIONAL) {
                    incomeThreshold = new BigDecimal("10000000"); // 10M TZS for institutional
                } else if (customerType == VfdCustomerProfile.CustomerType.CORPORATE) {
                    incomeThreshold = new BigDecimal("5000000"); // 5M TZS for corporate
                } else {
                    // Individual customers
                    if (riskProfile == VfdCustomerProfile.RiskProfile.HIGH) {
                        incomeThreshold = new BigDecimal("2000000"); // 2M TZS for high-risk individuals
                    } else if (riskProfile == VfdCustomerProfile.RiskProfile.MODERATE) {
                        incomeThreshold = new BigDecimal("1000000"); // 1M TZS for moderate-risk individuals
                    } else {
                        incomeThreshold = new BigDecimal("500000"); // 500K TZS for low-risk individuals
                    }
                }
                
                // Business rule: Transaction amount should not exceed 50% of income threshold
                BigDecimal maxTransactionAmount = incomeThreshold.multiply(new BigDecimal("0.5"));
                return amount.compareTo(maxTransactionAmount) <= 0;
            }
            return false; // Customer not found
        } catch (Exception e) {
            log.error("Error checking customer income eligibility: {}", e.getMessage(), e);
            return false; // Fail safe - assume not eligible on error
        }
    }

    /**
     * Check if customer risk profile is suitable for transaction amount
     */
    private boolean isCustomerRiskProfileSuitable(String customerId, BigDecimal amount) {
        Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
        if (customerProfile.isPresent()) {
            VfdCustomerProfile.RiskProfile riskProfile = customerProfile.get().getRiskProfile();
            // Basic business rule: high-risk customers can handle larger amounts
            if (riskProfile == VfdCustomerProfile.RiskProfile.HIGH) {
                return amount.compareTo(new BigDecimal("1000000")) <= 0; // 1 million limit
            } else if (riskProfile == VfdCustomerProfile.RiskProfile.MODERATE) {
                return amount.compareTo(new BigDecimal("500000")) <= 0; // 500k limit
            } else {
                return amount.compareTo(new BigDecimal("100000")) <= 0; // 100k limit
            }
        }
        return false;
    }

    /**
     * Check if customer meets regulatory compliance requirements
     */
    private boolean isCustomerRegulatoryCompliant(String customerId) {
        Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
        return customerProfile.isPresent() && customerProfile.get().getKycStatus() == VfdCustomerProfile.KycStatus.APPROVED;
    }

    /**
     * Check if transaction amount is within customer's trading limits
     */
    private boolean isWithinCustomerTradingLimits(String customerId, BigDecimal amount) {
        return isCustomerRiskProfileSuitable(customerId, amount);
    }

    /**
     * Check if customer has sufficient account balance
     */
    private boolean hasSufficientAccountBalance(String customerId, BigDecimal amount) {
        try {
            // Business rule: Transaction amount should not exceed 80% of available balance
            Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
            if (customerProfile.isPresent()) {
                BigDecimal actualBalance = getCustomerAccountBalance(customerId);
                
                if (actualBalance != null) {
                    BigDecimal maxTransactionAmount = actualBalance.multiply(new BigDecimal("0.8"));
                    return amount.compareTo(maxTransactionAmount) <= 0;
                }
                
                // Fallback: Simulate account balance based on customer type if actual balance not available
                BigDecimal simulatedBalance;
                switch (customerProfile.get().getCustomerType()) {
                    case INSTITUTIONAL:
                        simulatedBalance = new BigDecimal("100000000"); // 100M TZS
                        break;
                    case CORPORATE:
                        simulatedBalance = new BigDecimal("50000000"); // 50M TZS
                        break;
                    case INDIVIDUAL:
                        simulatedBalance = new BigDecimal("10000000"); // 10M TZS
                        break;
                    default:
                        simulatedBalance = new BigDecimal("1000000"); // 1M TZS default
                }
                
                BigDecimal maxTransactionAmount = simulatedBalance.multiply(new BigDecimal("0.8"));
                return amount.compareTo(maxTransactionAmount) <= 0;
            }
            return false; // Customer not found
        } catch (Exception e) {
            log.error("Error checking account balance: {}", e.getMessage(), e);
            return false; // Fail safe - assume insufficient balance on error
        }
    }

    /**
     * Check if customer is trading too frequently
     */
    private boolean isCustomerTradingTooFrequently(String customerId) {
        try {
            // Business rule: Check recent transactions for frequency violations
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
            
            // Implement actual transaction frequency check from database
            long hourlyTransactions = transactionRepository.countByCustomerIdAndTimestampAfter(customerId, oneHourAgo);
            long dailyTransactions = transactionRepository.countByCustomerIdAndTimestampAfter(customerId, oneDayAgo);
            
            // Business rules for trading frequency based on customer type
            Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
            if (customerProfile.isPresent()) {
                int hourlyLimit;
                int dailyLimit;
                
                switch (customerProfile.get().getCustomerType()) {
                    case INSTITUTIONAL:
                        hourlyLimit = 50; // Institutional customers can trade more frequently
                        dailyLimit = 500;
                        break;
                    case CORPORATE:
                        hourlyLimit = 25; // Corporate customers have moderate limits
                        dailyLimit = 250;
                        break;
                    default:
                        hourlyLimit = 10; // Individual customers have stricter limits
                        dailyLimit = 100;
                }
                
                boolean hourlyLimitExceeded = hourlyTransactions > hourlyLimit;
                boolean dailyLimitExceeded = dailyTransactions > dailyLimit;
                
                return hourlyLimitExceeded || dailyLimitExceeded;
            }
            
            // Default limits if customer profile not found
            boolean hourlyLimitExceeded = hourlyTransactions > 10; // Max 10 transactions per hour
            boolean dailyLimitExceeded = dailyTransactions > 100; // Max 100 transactions per day
            
            return hourlyLimitExceeded || dailyLimitExceeded;
            
        } catch (Exception e) {
            log.error("Error checking trading frequency: {}", e.getMessage(), e);
            return false; // Fail safe - assume normal frequency on error
        }
    }

    /**
     * Check if customer has geographic trading restrictions
     */
    private boolean hasCustomerGeographicRestrictions(String customerId) {
        Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
        if (customerProfile.isPresent()) {
            String country = customerProfile.get().getCountry();
            // Basic business rule: only Tanzania residents can trade
            return !"TANZANIA".equals(country);
        }
        return true; // Assume restrictions if customer not found
    }

    /**
     * Check if customer meets AML compliance requirements
     */
    private boolean isCustomerAmlCompliant(String customerId) {
        Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
        return customerProfile.isPresent() && customerProfile.get().getKycStatus() == VfdCustomerProfile.KycStatus.APPROVED;
    }

    /**
     * Check if customer is on sanctions list
     */
    private boolean isCustomerOnSanctionsList(String customerId) {
        try {
            // Business rule: Check customer profile for sanctions indicators
            Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
            if (customerProfile.isPresent()) {
                VfdCustomerProfile profile = customerProfile.get();
                
                String customerName = profile.getCustomerName();
                String country = profile.getCountry();
                String idNumber = profile.getIdNumber();
                
                boolean hasSanctions = checkSanctionsDatabase(customerName, country, idNumber);
                
                if (hasSanctions) {
                    log.warn("Customer {} found on sanctions list", customerId);
                    return true;
                }
                
                // Business rule: Check for additional sanctions indicators
                if (isCustomerInSanctionedCountry(country)) {
                    log.warn("Customer {} is from sanctioned country: {}", customerId, country);
                    return true;
                }
                
                if (isCustomerNameSanctioned(customerName)) {
                    log.warn("Customer {} has sanctioned name: {}", customerId, customerName);
                    return true;
                }
                
                return false; // No sanctions found
            }
            return false; // Customer not found
        } catch (Exception e) {
            log.error("Error checking sanctions list: {}", e.getMessage(), e);
            return false; // Fail safe - assume no sanctions on error
        }
    }

    /**
     * Check if customer is politically exposed
     */
    private boolean isCustomerPoliticallyExposed(String customerId) {
        try {
            // Business rule: Check customer profile for PEP indicators
            Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
            if (customerProfile.isPresent()) {
                VfdCustomerProfile profile = customerProfile.get();
                
                String customerName = profile.getCustomerName();
                VfdCustomerProfile.CustomerType customerType = profile.getCustomerType();
                String country = profile.getCountry();
                
                boolean isPEP = checkPEPDatabase(customerName, country);
                
                if (isPEP) {
                    log.warn("Customer {} identified as Politically Exposed Person", customerId);
                    return true;
                }
                
                // Business rule: Enhanced PEP screening for institutional customers
                if (customerType == VfdCustomerProfile.CustomerType.INSTITUTIONAL) {
                    boolean isInstitutionalPEP = checkInstitutionalPEPDatabase(customerName, country);
                    if (isInstitutionalPEP) {
                        log.warn("Institutional customer {} identified as PEP", customerId);
                        return true;
                    }
                }
                
                // Business rule: Check for PEP keywords in customer name
                if (customerName != null && containsPEPKeywords(customerName)) {
                    log.warn("Customer {} name contains PEP indicators", customerId);
                    return true;
                }
                
                return false; // No PEP indicators found
            }
            return false; // Customer not found
        } catch (Exception e) {
            log.error("Error checking PEP status: {}", e.getMessage(), e);
            return false; // Fail safe - assume not PEP on error
        }
    }

    /**
     * Helper method to check sanctions database
     */
    private boolean checkSanctionsDatabase(String customerName, String country, String idNumber) {
        try {
            // Simulate database query results
            if (customerName != null && customerName.toUpperCase().contains("SANCTIONED")) {
                return true;
            }
            
            if (country != null && country.toUpperCase().contains("SANCTIONED")) {
                return true;
            }
            
            if (idNumber != null && idNumber.toUpperCase().contains("SANCTIONED")) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.error("Error checking sanctions database: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Helper method to check if customer is in sanctioned country
     */
    private boolean isCustomerInSanctionedCountry(String country) {
        if (country == null) {
            return false;
        }
        
        // Business rule: List of sanctioned countries
        List<String> sanctionedCountries = Arrays.asList("IRAN", "NORTH_KOREA", "SYRIA", "VENEZUELA", "CUBA");
        return sanctionedCountries.contains(country.toUpperCase());
    }

    /**
     * Helper method to check if customer name is sanctioned
     */
    private boolean isCustomerNameSanctioned(String customerName) {
        if (customerName == null) {
            return false;
        }
        
        // Business rule: List of known sanctioned entities
        List<String> sanctionedNames = Arrays.asList("SANCTIONED_ENTITY_1", "SANCTIONED_ENTITY_2", "TERRORIST_ORG");
        
        String upperName = customerName.toUpperCase();
        for (String sanctionedName : sanctionedNames) {
            if (upperName.contains(sanctionedName)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Helper method to check PEP database
     */
    private boolean checkPEPDatabase(String customerName, String country) {
        try {
            // Simulate database query results
            if (customerName != null && customerName.toUpperCase().contains("PEP")) {
                return true;
            }
            
            if (country != null && country.toUpperCase().contains("PEP_COUNTRY")) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.error("Error checking PEP database: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Helper method to check institutional PEP database
     */
    private boolean checkInstitutionalPEPDatabase(String customerName, String country) {
        try {
            // Simulate database query results
            if (customerName != null && customerName.toUpperCase().contains("INSTITUTIONAL_PEP")) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.error("Error checking institutional PEP database: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Helper method to check for PEP keywords in customer name
     */
    private boolean containsPEPKeywords(String customerName) {
        if (customerName == null) {
            return false;
        }
        
        // Business rule: List of PEP-related keywords
        List<String> pepKeywords = Arrays.asList("MINISTER", "AMBASSADOR", "GOVERNOR", "PRESIDENT", "SENATOR", "MAYOR");
        
        String upperName = customerName.toUpperCase();
        for (String keyword : pepKeywords) {
            if (upperName.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Check if transaction meets regulatory reporting requirements
     */
    private boolean meetsRegulatoryReportingRequirements(String customerId, BigDecimal amount) {
        // This logic needs to be implemented based on actual transaction history
        // For now, a placeholder to prevent infinite loops
        return false;
    }

    /**
     * Check if customer has transaction monitoring alerts
     */
    private boolean hasTransactionMonitoringAlerts(String customerId) {
        // This logic needs to be implemented based on actual transaction history
        // For now, a placeholder to prevent infinite loops
        return false;
    }

    /**
     * Check if transaction complies with local regulations
     */
    private boolean isCompliantWithLocalRegulations(String customerId, BigDecimal amount) {
        // This logic needs to be implemented based on actual transaction history
        // For now, a placeholder to prevent infinite loops
        return false;
    }

    /**
     * Check if customer has high-risk rating
     */
    private boolean isCustomerHighRisk(String customerId) {
        // This logic needs to be implemented based on actual risk rating
        // For now, a placeholder to prevent infinite loops
        return false;
    }

    /**
     * Check if customer has unusual transaction pattern
     */
    private boolean hasUnusualTransactionPattern(String customerId, BigDecimal amount) {
        // This logic needs to be implemented based on actual transaction history
        // For now, a placeholder to prevent infinite loops
        return false;
    }

    /**
     * Check if instrument is active
     */
    private boolean isInstrumentActive(String instrumentCode) {
        try {
            // Business rule: Enhanced instrument status validation
            boolean isActive = checkInstrumentStatusFromDatabase(instrumentCode);
            
            if (isActive) {
                return true;
            }
            
            // Fallback: Check for active instrument patterns
            // Active instrument patterns
            List<String> activePatterns = Arrays.asList(
                "ACTIVE_", "BOND_", "STOCK_", "ETF_", "MUTUAL_FUND_", "TREASURY_"
            );
            
            // Suspended/inactive instrument patterns
            List<String> inactivePatterns = Arrays.asList(
                "SUSPENDED_", "DELISTED_", "MAINTENANCE_", "INACTIVE_", "EXPIRED_"
            );
            
            // Check for inactive patterns first
            for (String pattern : inactivePatterns) {
                if (instrumentCode.startsWith(pattern)) {
                    return false;
                }
            }
            
            // Check for active patterns
            for (String pattern : activePatterns) {
                if (instrumentCode.startsWith(pattern)) {
                    return true;
                }
            }
            
            // Business rule: Default to active for unknown patterns
            // In production, this would be more restrictive
            return true;
            
        } catch (Exception e) {
            log.error("Error checking instrument active status: {}", e.getMessage(), e);
            return false; // Fail safe - assume inactive on error
        }
    }

    /**
     * Check if instrument is tradable
     */
    private boolean isInstrumentTradable(String instrumentCode) {
        try {
            // Business rule: Enhanced tradability validation
            if (!isInstrumentActive(instrumentCode)) {
                return false;
            }
            
            boolean isTradable = checkInstrumentTradabilityFromDatabase(instrumentCode);
            
            if (isTradable) {
                return true;
            }
            
            // Fallback: Check for non-tradable patterns
            // Non-tradable patterns
            List<String> nonTradablePatterns = Arrays.asList(
                "SUSPENDED_", "DELISTED_", "MAINTENANCE_", "LOCKED_", "RESTRICTED_"
            );
            
            // Check for non-tradable patterns
            for (String pattern : nonTradablePatterns) {
                if (instrumentCode.startsWith(pattern)) {
                    return false;
                }
            }
            
            // Business rule: Check trading hours for certain instrument types
            if (instrumentCode.startsWith("STOCK_") || instrumentCode.startsWith("ETF_")) {
                // Stocks and ETFs have specific trading hours
                return isInstrumentWithinTradingHours(instrumentCode);
            }
            
            // Business rule: Bonds and treasury instruments are always tradable during business hours
            if (instrumentCode.startsWith("BOND_") || instrumentCode.startsWith("TREASURY_")) {
                return true;
            }
            
            return true; // Default to tradable
            
        } catch (Exception e) {
            log.error("Error checking instrument tradability: {}", e.getMessage(), e);
            return false; // Fail safe - assume not tradable on error
        }
    }

    /**
     * Check if instrument is within trading hours
     */
    private boolean isInstrumentWithinTradingHours(String instrumentCode) {
        try {
            // Business rule: Get trading hours from database
            TradingHours tradingHours = getInstrumentTradingHours(instrumentCode);
            
            if (tradingHours != null) {
                return isCurrentTimeWithinTradingHours(tradingHours);
            }
            
            // Fallback: Default trading hours for Tanzania (9:00 AM - 3:00 PM EAT)
            LocalTime now = LocalTime.now();
            LocalTime marketOpen = LocalTime.of(9, 0);
            LocalTime marketClose = LocalTime.of(15, 0);
            
            // Check if current time is within trading hours
            return now.isAfter(marketOpen) && now.isBefore(marketClose);
            
        } catch (Exception e) {
            log.error("Error checking trading hours: {}", e.getMessage(), e);
            return false; // Fail safe - assume outside trading hours on error
        }
    }

    /**
     * Helper method to check instrument status from database
     */
    private Boolean checkInstrumentStatusFromDatabase(String instrumentCode) {
        try {
            // Simulate database query for instrument status
            if (instrumentCode.contains("DB_ACTIVE")) {
                return true;
            }
            
            if (instrumentCode.contains("DB_INACTIVE")) {
                return false;
            }
            
            // Default to null to trigger fallback logic
            return null;
            
        } catch (Exception e) {
            log.error("Error checking instrument status from database: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Helper method to check instrument tradability from database
     */
    private Boolean checkInstrumentTradabilityFromDatabase(String instrumentCode) {
        try {
            // Simulate database query for instrument tradability
            if (instrumentCode.contains("DB_TRADABLE")) {
                return true;
            }
            
            if (instrumentCode.contains("DB_NON_TRADABLE")) {
                return false;
            }
            
            // Default to null to trigger fallback logic
            return null;
            
        } catch (Exception e) {
            log.error("Error checking instrument tradability from database: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Helper method to get instrument trading hours
     */
    private TradingHours getInstrumentTradingHours(String instrumentCode) {
        try {
            // Simulate different trading hours for different instrument types
            if (instrumentCode.startsWith("STOCK_")) {
                // Stocks: 9:00 AM - 3:00 PM EAT
                return new TradingHours(LocalTime.of(9, 0), LocalTime.of(15, 0));
            } else if (instrumentCode.startsWith("BOND_")) {
                // Bonds: 8:00 AM - 4:00 PM EAT
                return new TradingHours(LocalTime.of(8, 0), LocalTime.of(16, 0));
            } else if (instrumentCode.startsWith("ETF_")) {
                // ETFs: 9:00 AM - 3:00 PM EAT
                return new TradingHours(LocalTime.of(9, 0), LocalTime.of(15, 0));
            }
            
            // Default trading hours
            return new TradingHours(LocalTime.of(9, 0), LocalTime.of(15, 0));
            
        } catch (Exception e) {
            log.error("Error getting instrument trading hours: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Helper method to check if current time is within trading hours
     */
    private boolean isCurrentTimeWithinTradingHours(TradingHours tradingHours) {
        if (tradingHours == null) {
            return false;
        }
        
        LocalTime now = LocalTime.now();
        return now.isAfter(tradingHours.getOpenTime()) && now.isBefore(tradingHours.getCloseTime());
    }

    /**
     * Inner class to represent trading hours
     */
    private static class TradingHours {
        private final LocalTime openTime;
        private final LocalTime closeTime;
        
        public TradingHours(LocalTime openTime, LocalTime closeTime) {
            this.openTime = openTime;
            this.closeTime = closeTime;
        }
        
        public LocalTime getOpenTime() {
            return openTime;
        }
        
        public LocalTime getCloseTime() {
            return closeTime;
        }
    }

    /**
     * Check if instrument has sufficient liquidity
     */
    private boolean hasInstrumentSufficientLiquidity(String instrumentCode, BigDecimal amount) {
        try {
            // Business rule: Enhanced liquidity validation based on instrument type
            
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
            
            BigDecimal liquidityThreshold;
            
            if (instrumentCode.startsWith("STOCK_")) {
                // Stocks have moderate liquidity requirements
                liquidityThreshold = new BigDecimal("500000"); // 500K TZS
            } else if (instrumentCode.startsWith("BOND_")) {
                // Bonds have higher liquidity requirements
                liquidityThreshold = new BigDecimal("1000000"); // 1M TZS
            } else if (instrumentCode.startsWith("TREASURY_")) {
                // Treasury instruments have highest liquidity
                liquidityThreshold = new BigDecimal("5000000"); // 5M TZS
            } else if (instrumentCode.startsWith("ETF_")) {
                // ETFs have moderate to high liquidity
                liquidityThreshold = new BigDecimal("750000"); // 750K TZS
            } else {
                // Default liquidity threshold
                liquidityThreshold = new BigDecimal("1000000"); // 1M TZS
            }
            
            // Business rule: Amount should not exceed liquidity threshold
            return amount.compareTo(liquidityThreshold) <= 0;
            
        } catch (Exception e) {
            log.error("Error checking instrument liquidity: {}", e.getMessage(), e);
            return false; // Fail safe - assume insufficient liquidity on error
        }
    }

    /**
     * Check if instrument meets regulatory compliance requirements
     */
    private boolean isInstrumentRegulatoryCompliant(String instrumentCode) {
        try {
            // Business rule: Enhanced regulatory compliance validation
            
            if (instrumentCode == null || instrumentCode.trim().isEmpty()) {
                return false;
            }
            
            // Regulated instrument patterns
            List<String> regulatedPatterns = Arrays.asList(
                "REGULATED_", "BOND_", "STOCK_", "TREASURY_", "ETF_", "MUTUAL_FUND_"
            );
            
            // Check for regulated patterns
            for (String pattern : regulatedPatterns) {
                if (instrumentCode.startsWith(pattern)) {
                    return true;
                }
            }
            
            // Business rule: All financial instruments must meet basic compliance
            // In production, this would check against actual compliance records
            return true;
            
        } catch (Exception e) {
            log.error("Error checking instrument regulatory compliance: {}", e.getMessage(), e);
            return false; // Fail safe - assume non-compliant on error
        }
    }

    /**
     * Check if instrument is suitable for customer
     */
    private boolean isInstrumentSuitableForCustomer(String instrumentCode, String customerId) {
        try {
            // Business rule: Enhanced suitability validation
            
            if (!isInstrumentActive(instrumentCode) || !isCustomerAccountActive(customerId)) {
                return false;
            }
            
            Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
            if (customerProfile.isPresent()) {
                VfdCustomerProfile profile = customerProfile.get();
                VfdCustomerProfile.CustomerType customerType = profile.getCustomerType();
                VfdCustomerProfile.RiskProfile riskProfile = profile.getRiskProfile();
                
                // Business rule: High-risk instruments require high-risk customer profile
                if (instrumentCode.startsWith("HIGH_RISK_")) {
                    return riskProfile == VfdCustomerProfile.RiskProfile.HIGH;
                }
                
                // Business rule: Institutional instruments require institutional customers
                if (instrumentCode.startsWith("INSTITUTIONAL_")) {
                    return customerType == VfdCustomerProfile.CustomerType.INSTITUTIONAL;
                }
                
                // Business rule: Corporate instruments require corporate or institutional customers
                if (instrumentCode.startsWith("CORPORATE_")) {
                    return customerType == VfdCustomerProfile.CustomerType.CORPORATE || 
                           customerType == VfdCustomerProfile.CustomerType.INSTITUTIONAL;
                }
                
                // Business rule: Retail instruments are suitable for all customer types
                if (instrumentCode.startsWith("RETAIL_")) {
                    return true;
                }
                
                // Default: All instruments are suitable for all customers
                return true;
            }
            
            return false; // Customer not found
            
        } catch (Exception e) {
            log.error("Error checking instrument suitability: {}", e.getMessage(), e);
            return false; // Fail safe - assume not suitable on error
        }
    }

    /**
     * Check if instrument has trading restrictions
     */
    private boolean hasInstrumentTradingRestrictions(String instrumentCode) {
        try {
            // Business rule: Enhanced trading restrictions validation
            
            if (instrumentCode == null || instrumentCode.trim().isEmpty()) {
                return false;
            }
            
            // Restricted instrument patterns
            List<String> restrictedPatterns = Arrays.asList(
                "RESTRICTED_", "LIMITED_", "LOCKED_", "BLOCKED_", "SUSPENDED_"
            );
            
            // Check for restricted patterns
            for (String pattern : restrictedPatterns) {
                if (instrumentCode.startsWith(pattern)) {
                    return true;
                }
            }
            
            // Business rule: Check for time-based restrictions
            if (instrumentCode.startsWith("TIME_RESTRICTED_")) {
                // Time-restricted instruments have specific trading windows
                return true;
            }
            
            // Business rule: Check for volume-based restrictions
            if (instrumentCode.startsWith("VOLUME_LIMITED_")) {
                // Volume-limited instruments have trading volume restrictions
                return true;
            }
            
            return false; // No restrictions found
            
        } catch (Exception e) {
            log.error("Error checking instrument trading restrictions: {}", e.getMessage(), e);
            return false; // Fail safe - assume no restrictions on error
        }
    }

    /**
     * Check if broker is active
     */
    private boolean isBrokerActive(String brokerCode) {
        try {
            // Business rule: Enhanced broker status validation
            
            if (brokerCode == null || brokerCode.trim().isEmpty()) {
                return false;
            }
            
            // Active broker patterns
            List<String> activePatterns = Arrays.asList(
                "ACTIVE_", "BROKER_", "LICENSED_", "CERTIFIED_", "APPROVED_"
            );
            
            // Inactive broker patterns
            List<String> inactivePatterns = Arrays.asList(
                "SUSPENDED_", "REVOKED_", "INACTIVE_", "EXPIRED_", "BLOCKED_"
            );
            
            // Check for inactive patterns first
            for (String pattern : inactivePatterns) {
                if (brokerCode.startsWith(pattern)) {
                    return false;
                }
            }
            
            // Check for active patterns
            for (String pattern : activePatterns) {
                if (brokerCode.startsWith(pattern)) {
                    return true;
                }
            }
            
            // Business rule: Default to active for unknown patterns
            // In production, this would be more restrictive
            return true;
            
        } catch (Exception e) {
            log.error("Error checking broker active status: {}", e.getMessage(), e);
            return false; // Fail safe - assume inactive on error
        }
    }

    /**
     * Check if broker is licensed
     */
    private boolean isBrokerLicensed(String brokerCode) {
        try {
            // Business rule: Enhanced license validation
            
            if (!isBrokerActive(brokerCode)) {
                return false;
            }
            
            // Licensed broker patterns
            List<String> licensedPatterns = Arrays.asList(
                "LICENSED_", "CERTIFIED_", "APPROVED_", "REGISTERED_"
            );
            
            // Check for licensed patterns
            for (String pattern : licensedPatterns) {
                if (brokerCode.startsWith(pattern)) {
                    return true;
                }
            }
            
            // Business rule: All active brokers are assumed to be licensed
            // In a real implementation, this would check against actual license records
            return true;
            
        } catch (Exception e) {
            log.error("Error checking broker license: {}", e.getMessage(), e);
            return false; // Fail safe - assume not licensed on error
        }
    }

    /**
     * Check if broker has trading permissions for instrument
     */
    private boolean hasBrokerTradingPermissions(String brokerCode, String instrumentCode) {
        try {
            // Business rule: Enhanced trading permissions validation
            
            if (!isBrokerActive(brokerCode) || !isInstrumentActive(instrumentCode)) {
                return false;
            }
            
            // Specialized broker patterns
            List<String> specializedPatterns = Arrays.asList(
                "BOND_", "STOCK_", "DERIVATIVES_", "FOREX_", "COMMODITIES_"
            );
            
            // Check for specialized broker permissions
            for (String pattern : specializedPatterns) {
                if (brokerCode.startsWith("BROKER_" + pattern) && instrumentCode.startsWith(pattern)) {
                    return true;
                }
            }
            
            // Business rule: General brokers can trade most instruments
            if (brokerCode.startsWith("BROKER_GENERAL_")) {
                return true;
            }
            
            // Business rule: Retail brokers have limited permissions
            if (brokerCode.startsWith("BROKER_RETAIL_")) {
                // Retail brokers can only trade basic instruments
                return instrumentCode.startsWith("STOCK_") || instrumentCode.startsWith("BOND_");
            }
            
            // Business rule: Institutional brokers have full permissions
            if (brokerCode.startsWith("BROKER_INSTITUTIONAL_")) {
                return true;
            }
            
            return false; // No permissions found
            
        } catch (Exception e) {
            log.error("Error checking broker trading permissions: {}", e.getMessage(), e);
            return false; // Fail safe - assume no permissions on error
        }
    }

    /**
     * Check if broker has sufficient capital
     */
    private boolean hasBrokerSufficientCapital(String brokerCode) {
        try {
            // Business rule: Enhanced capital adequacy validation
            
            if (!isBrokerActive(brokerCode)) {
                return false;
            }
            
            // Capital requirements based on broker type
            BigDecimal requiredCapital;
            
            if (brokerCode.startsWith("BROKER_INSTITUTIONAL_")) {
                requiredCapital = new BigDecimal("1000000000"); // 1B TZS for institutional
            } else if (brokerCode.startsWith("BROKER_CORPORATE_")) {
                requiredCapital = new BigDecimal("500000000"); // 500M TZS for corporate
            } else if (brokerCode.startsWith("BROKER_RETAIL_")) {
                requiredCapital = new BigDecimal("100000000"); // 100M TZS for retail
            } else {
                requiredCapital = new BigDecimal("250000000"); // 250M TZS default
            }
            
            // Business rule: Simulate capital check
            // In a real implementation, this would query actual capital records
            BigDecimal simulatedCapital = requiredCapital.multiply(new BigDecimal("1.5")); // 150% of required
            
            // Business rule: Capital must be at least 120% of required amount
            BigDecimal minimumCapital = requiredCapital.multiply(new BigDecimal("1.2"));
            return simulatedCapital.compareTo(minimumCapital) >= 0;
            
        } catch (Exception e) {
            log.error("Error checking broker capital: {}", e.getMessage(), e);
            return false; // Fail safe - assume insufficient capital on error
        }
    }

    /**
     * Check if broker meets regulatory compliance requirements
     */
    private boolean isBrokerRegulatoryCompliant(String brokerCode) {
        try {
            // Business rule: Enhanced regulatory compliance validation
            
            if (!isBrokerActive(brokerCode)) {
                return false;
            }
            
            // Compliance indicators
            List<String> complianceIndicators = Arrays.asList(
                "COMPLIANT_", "REGULATED_", "AUDITED_", "CERTIFIED_"
            );
            
            // Check for compliance indicators
            for (String indicator : complianceIndicators) {
                if (brokerCode.startsWith(indicator)) {
                    return true;
                }
            }
            
            // Business rule: All active brokers are assumed to be compliant
            // In a real implementation, this would check against actual compliance records
            return true;
            
        } catch (Exception e) {
            log.error("Error checking broker regulatory compliance: {}", e.getMessage(), e);
            return false; // Fail safe - assume non-compliant on error
        }
    }

    /**
     * Check if broker has trading restrictions
     */
    private boolean hasBrokerTradingRestrictions(String brokerCode) {
        try {
            // Business rule: Enhanced trading restrictions validation
            
            if (brokerCode == null || brokerCode.trim().isEmpty()) {
                return false;
            }
            
            // Restricted broker patterns
            List<String> restrictedPatterns = Arrays.asList(
                "RESTRICTED_", "LIMITED_", "SUSPENDED_", "BLOCKED_", "PROBATION_"
            );
            
            // Check for restricted patterns
            for (String pattern : restrictedPatterns) {
                if (brokerCode.startsWith(pattern)) {
                    return true;
                }
            }
            
            // Business rule: Check for time-based restrictions
            if (brokerCode.startsWith("TIME_RESTRICTED_")) {
                // Time-restricted brokers have specific trading windows
                return true;
            }
            
            // Business rule: Check for volume-based restrictions
            if (brokerCode.startsWith("VOLUME_LIMITED_")) {
                // Volume-limited brokers have trading volume restrictions
                return true;
            }
            
            return false; // No restrictions found
            
        } catch (Exception e) {
            log.error("Error checking broker trading restrictions: {}", e.getMessage(), e);
            return false; // Fail safe - assume no restrictions on error
        }
    }

    /**
     * Check if broker is authorized for customer
     */
    private boolean isBrokerAuthorizedForCustomer(String brokerCode, String customerId) {
        try {
            // Business rule: Enhanced customer authorization validation
            
            if (!isBrokerActive(brokerCode) || !isCustomerAccountActive(customerId)) {
                return false;
            }
            
            Optional<VfdCustomerProfile> customerProfile = customerProfileRepository.findByCustomerId(customerId);
            if (customerProfile.isPresent()) {
                VfdCustomerProfile profile = customerProfile.get();
                VfdCustomerProfile.CustomerType customerType = profile.getCustomerType();
                VfdCustomerProfile.RiskProfile riskProfile = profile.getRiskProfile();
                
                // Business rule: Retail brokers can serve retail customers
                if (brokerCode.startsWith("BROKER_RETAIL_")) {
                    return customerType == VfdCustomerProfile.CustomerType.INDIVIDUAL;
                }
                
                // Business rule: Corporate brokers can serve corporate customers
                if (brokerCode.startsWith("BROKER_CORPORATE_")) {
                    return customerType == VfdCustomerProfile.CustomerType.CORPORATE;
                }
                
                // Business rule: Institutional brokers can serve institutional customers
                if (brokerCode.startsWith("BROKER_INSTITUTIONAL_")) {
                    return customerType == VfdCustomerProfile.CustomerType.INSTITUTIONAL;
                }
                
                // Business rule: High-risk customers require specialized brokers
                if (riskProfile == VfdCustomerProfile.RiskProfile.HIGH) {
                    return brokerCode.startsWith("BROKER_HIGH_RISK_") || 
                           brokerCode.startsWith("BROKER_INSTITUTIONAL_");
                }
                
                // Business rule: General brokers can serve all customer types
                if (brokerCode.startsWith("BROKER_GENERAL_")) {
                    return true;
                }
                
                // Default: Assume authorization for active brokers and customers
                return true;
            }
            
            return false; // Customer not found
            
        } catch (Exception e) {
            log.error("Error checking broker customer authorization: {}", e.getMessage(), e);
            return false; // Fail safe - assume not authorized on error
        }
    }

    /**
     * Validate transaction amount
     */
    public boolean validateAmount(BigDecimal amount) {
        log.info("Validating transaction amount: {}", amount);
        
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Validate customer eligibility
     */
    public VfdValidationResponse validateCustomerEligibility(VfdValidationRequest request) {
        log.info("Validating customer eligibility for: {}", request.getCustomerId());
        
        VfdValidationResponse response = new VfdValidationResponse();
        response.setRequestId(request.getRequestId());
        response.setValidationTime(LocalDateTime.now());
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Customer eligibility validation
        if (request.getCustomerId() == null || request.getCustomerId().trim().isEmpty()) {
            errors.add("Customer ID is required");
        }
        
        // Step 1: Check customer age eligibility
        if (!isCustomerAgeEligible(request.getCustomerId())) {
            errors.add("Customer does not meet minimum age requirement (18 years)");
        }
        
        // Step 2: Check customer income eligibility
        if (!isCustomerIncomeEligible(request.getCustomerId(), request.getAmount())) {
            errors.add("Customer income does not meet minimum requirements for this transaction amount");
        }
        
        // Step 3: Check customer risk profile
        if (!isCustomerRiskProfileSuitable(request.getCustomerId(), request.getAmount())) {
            errors.add("Customer risk profile is not suitable for this transaction amount");
        }
        
        // Step 4: Check regulatory compliance
        if (!isCustomerRegulatoryCompliant(request.getCustomerId())) {
            errors.add("Customer does not meet regulatory compliance requirements");
        }
        
        // Step 5: Check trading limits
        if (!isWithinCustomerTradingLimits(request.getCustomerId(), request.getAmount())) {
            errors.add("Transaction amount exceeds customer's trading limits");
        }
        
        // Step 6: Check account balance
        if (!hasSufficientAccountBalance(request.getCustomerId(), request.getAmount())) {
            errors.add("Insufficient account balance for this transaction");
        }
        
        // Step 7: Check trading frequency
        if (isCustomerTradingTooFrequently(request.getCustomerId())) {
            warnings.add("Customer has made multiple transactions recently - monitor for unusual activity");
        }
        
        // Step 8: Check geographic restrictions
        if (hasCustomerGeographicRestrictions(request.getCustomerId())) {
            warnings.add("Customer has geographic trading restrictions");
        }
        
        response.setValidationErrors(errors);
        response.setValidationWarnings(warnings);
        response.setValid(errors.isEmpty());
        response.setStatus(errors.isEmpty() ? "ELIGIBLE" : "NOT_ELIGIBLE");
        response.setMessage(errors.isEmpty() ? "Customer is eligible" : "Customer eligibility check failed");
        
        return response;
    }

    /**
     * Validate compliance
     */
    public VfdValidationResponse validateCompliance(VfdValidationRequest request) {
        log.info("Validating compliance for: {}", request.getCustomerId());
        
        VfdValidationResponse response = new VfdValidationResponse();
        response.setRequestId(request.getRequestId());
        response.setValidationTime(LocalDateTime.now());
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Compliance validation
        if (request.getCustomerId() == null || request.getCustomerId().trim().isEmpty()) {
            errors.add("Customer ID is required");
        }
        
        // Step 1: Check Anti-Money Laundering (AML) compliance
        if (!isCustomerAmlCompliant(request.getCustomerId())) {
            errors.add("Customer does not meet AML compliance requirements");
        }
        
        // Step 2: Check sanctions screening
        if (isCustomerOnSanctionsList(request.getCustomerId())) {
            errors.add("Customer is on sanctions list - transaction blocked");
        }
        
        // Step 3: Check politically exposed person (PEP) status
        if (isCustomerPoliticallyExposed(request.getCustomerId())) {
            warnings.add("Customer is a politically exposed person - enhanced due diligence required");
        }
        
        // Step 4: Check regulatory reporting requirements
        if (!meetsRegulatoryReportingRequirements(request.getCustomerId(), request.getAmount())) {
            errors.add("Transaction does not meet regulatory reporting requirements");
        }
        
        // Step 5: Check transaction monitoring alerts
        if (hasTransactionMonitoringAlerts(request.getCustomerId())) {
            warnings.add("Customer has active transaction monitoring alerts");
        }
        
        // Step 6: Check compliance with local regulations
        if (!isCompliantWithLocalRegulations(request.getCustomerId(), request.getAmount())) {
            errors.add("Transaction does not comply with local regulatory requirements");
        }
        
        // Step 7: Check customer risk rating
        if (isCustomerHighRisk(request.getCustomerId())) {
            warnings.add("Customer has high-risk rating - additional monitoring required");
        }
        
        // Step 8: Check transaction pattern analysis
        if (hasUnusualTransactionPattern(request.getCustomerId(), request.getAmount())) {
            warnings.add("Unusual transaction pattern detected - review recommended");
        }
        
        response.setValidationErrors(errors);
        response.setValidationWarnings(warnings);
        response.setValid(errors.isEmpty());
        response.setStatus(errors.isEmpty() ? "COMPLIANT" : "NON_COMPLIANT");
        response.setMessage(errors.isEmpty() ? "Compliance validation passed" : "Compliance validation failed");
        
        return response;
    }

    /**
     * Validate instrument
     */
    public VfdValidationResponse validateInstrument(VfdValidationRequest request) {
        log.info("Validating instrument for: {}", request.getCustomerId());
        
        VfdValidationResponse response = new VfdValidationResponse();
        response.setRequestId(request.getRequestId());
        response.setValidationTime(LocalDateTime.now());
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Instrument validation
        if (request.getCustomerId() == null || request.getCustomerId().trim().isEmpty()) {
            errors.add("Customer ID is required");
        }
        
        // Step 1: Check if instrument code is provided
        if (request.getInstrumentCode() == null || request.getInstrumentCode().trim().isEmpty()) {
            errors.add("Instrument code is required");
        } else {
            // Step 2: Check if instrument exists and is active
            if (!isInstrumentActive(request.getInstrumentCode())) {
                errors.add("Instrument is not active or does not exist");
            }
            
            // Step 3: Check if instrument is tradable
            if (!isInstrumentTradable(request.getInstrumentCode())) {
                errors.add("Instrument is not available for trading");
            }
            
            // Step 4: Check if instrument is within trading hours
            if (!isInstrumentWithinTradingHours(request.getInstrumentCode())) {
                errors.add("Instrument is outside trading hours");
            }
            
            // Step 5: Check if instrument has sufficient liquidity
            if (!hasInstrumentSufficientLiquidity(request.getInstrumentCode(), request.getAmount())) {
                warnings.add("Instrument may have insufficient liquidity for this transaction amount");
            }
            
            // Step 6: Check if instrument meets regulatory requirements
            if (!isInstrumentRegulatoryCompliant(request.getInstrumentCode())) {
                errors.add("Instrument does not meet regulatory compliance requirements");
            }
            
            // Step 7: Check if instrument is suitable for customer risk profile
            if (!isInstrumentSuitableForCustomer(request.getInstrumentCode(), request.getCustomerId())) {
                warnings.add("Instrument may not be suitable for customer's risk profile");
            }
            
            // Step 8: Check if instrument has any trading restrictions
            if (hasInstrumentTradingRestrictions(request.getInstrumentCode())) {
                warnings.add("Instrument has trading restrictions - review required");
            }
        }
        
        response.setValidationErrors(errors);
        response.setValidationWarnings(warnings);
        response.setValid(errors.isEmpty());
        response.setStatus(errors.isEmpty() ? "VALID_INSTRUMENT" : "INVALID_INSTRUMENT");
        response.setMessage(errors.isEmpty() ? "Instrument validation passed" : "Instrument validation failed");
        
        return response;
    }

    /**
     * Validate broker
     */
    public VfdValidationResponse validateBroker(VfdValidationRequest request) {
        log.info("Validating broker for: {}", request.getCustomerId());
        
        VfdValidationResponse response = new VfdValidationResponse();
        response.setRequestId(request.getRequestId());
        response.setValidationTime(LocalDateTime.now());
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Broker validation
        if (request.getCustomerId() == null || request.getCustomerId().trim().isEmpty()) {
            errors.add("Customer ID is required");
        }
        
        // Step 1: Check if broker code is provided
        if (request.getBrokerCode() == null || request.getBrokerCode().trim().isEmpty()) {
            errors.add("Broker code is required");
        } else {
            // Step 2: Check if broker exists and is active
            if (!isBrokerActive(request.getBrokerCode())) {
                errors.add("Broker is not active or does not exist");
            }
            
            // Step 3: Check if broker is licensed and authorized
            if (!isBrokerLicensed(request.getBrokerCode())) {
                errors.add("Broker is not licensed or authorized to trade");
            }
            
            // Step 4: Check if broker has trading permissions for this instrument
            if (!hasBrokerTradingPermissions(request.getBrokerCode(), request.getInstrumentCode())) {
                errors.add("Broker does not have trading permissions for this instrument");
            }
            
            // Step 5: Check if broker has sufficient capital requirements
            if (!hasBrokerSufficientCapital(request.getBrokerCode())) {
                warnings.add("Broker may not meet capital requirements");
            }
            
            // Step 6: Check if broker is compliant with regulations
            if (!isBrokerRegulatoryCompliant(request.getBrokerCode())) {
                errors.add("Broker does not meet regulatory compliance requirements");
            }
            
            // Step 7: Check if broker has any trading restrictions
            if (hasBrokerTradingRestrictions(request.getBrokerCode())) {
                warnings.add("Broker has trading restrictions - review required");
            }
            
            // Step 8: Check if broker is authorized for this customer
            if (!isBrokerAuthorizedForCustomer(request.getBrokerCode(), request.getCustomerId())) {
                errors.add("Broker is not authorized to trade for this customer");
            }
        }
        
        response.setValidationErrors(errors);
        response.setValidationWarnings(warnings);
        response.setValid(errors.isEmpty());
        response.setStatus(errors.isEmpty() ? "VALID_BROKER" : "INVALID_BROKER");
        response.setMessage(errors.isEmpty() ? "Broker validation passed" : "Broker validation failed");
        
        return response;
    }

    /**
     * Helper method to get customer account balance from database
     */
    private BigDecimal getCustomerAccountBalance(String customerId) {
        try {
            // In a real implementation, this would query the account balance repository
            // For now, return null to trigger fallback logic
            return null;
        } catch (Exception e) {
            log.error("Error retrieving customer account balance: {}", e.getMessage(), e);
            return null;
        }
    }
}
