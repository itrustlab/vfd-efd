package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdTransactionRequest;
import tz.co.itrust.vfd.dto.VfdValidationRequest;
import tz.co.itrust.vfd.dto.VfdValidationResponse;
import tz.co.itrust.vfd.services.VfdValidationService;

import java.util.Map;

/**
 * VFD Validation Controller
 * Handles VFD request validation and business rules
 */
@RestController
@RequestMapping("/api/vfd/validation")
@Tag(name = "VFD Validation", description = "VFD request validation and business rules")
@RequiredArgsConstructor
public class VfdValidationController extends BaseController {

    private final VfdValidationService vfdValidationService;

    /**
     * Validate VFD transaction request
     */
    @PostMapping("/transaction")
    @Operation(summary = "Validate Transaction", description = "Validate a VFD transaction request")
    public ResponseEntity<Map<String, Object>> validateTransaction(@RequestBody VfdTransactionRequest request) {
        logInfo("Validating VFD transaction request for customer: " + request.getCustomerId());
        
        try {
            VfdValidationResponse response = vfdValidationService.validateTransaction(request);
            return successResponse("Transaction validation completed", "200", null, response);
        } catch (Exception e) {
            logError("Error validating transaction: " + e.getMessage());
            return errorResponse("Failed to validate transaction", "500", null, null);
        }
    }

    /**
     * Validate VFD customer eligibility
     */
    @PostMapping("/customer-eligibility")
    @Operation(summary = "Validate Customer Eligibility", description = "Validate customer eligibility for VFD services")
    public ResponseEntity<Map<String, Object>> validateCustomerEligibility(@RequestBody VfdValidationRequest request) {
        logInfo("Validating customer eligibility: " + request.getCustomerId());
        
        try {
            VfdValidationResponse response = vfdValidationService.validateCustomerEligibility(request);
            return successResponse("Customer eligibility validation completed", "200", null, response);
        } catch (Exception e) {
            logError("Error validating customer eligibility: " + e.getMessage());
            return errorResponse("Failed to validate customer eligibility", "500", null, null);
        }
    }

    /**
     * Validate VFD compliance rules
     */
    @PostMapping("/compliance")
    @Operation(summary = "Validate Compliance", description = "Validate VFD compliance rules")
    public ResponseEntity<Map<String, Object>> validateCompliance(@RequestBody VfdValidationRequest request) {
        logInfo("Validating VFD compliance rules");
        
        try {
            VfdValidationResponse response = vfdValidationService.validateCompliance(request);
            return successResponse("Compliance validation completed", "200", null, response);
        } catch (Exception e) {
            logError("Error validating compliance: " + e.getMessage());
            return errorResponse("Failed to validate compliance", "500", null, null);
        }
    }

    /**
     * Get validation rules
     */
    @GetMapping("/rules")
    @Operation(summary = "Get Validation Rules", description = "Get VFD validation rules and business logic")
    public ResponseEntity<Map<String, Object>> getValidationRules() {
        logInfo("Fetching VFD validation rules");
        
        try {
            Map<String, Object> rules = vfdValidationService.getValidationRules();
            return successResponse("Validation rules retrieved successfully", "200", null, rules);
        } catch (Exception e) {
            logError("Error fetching validation rules: " + e.getMessage());
            return errorResponse("Failed to fetch validation rules", "500", null, null);
        }
    }

    /**
     * Validate VFD instrument
     */
    @PostMapping("/instrument")
    @Operation(summary = "Validate Instrument", description = "Validate VFD instrument details")
    public ResponseEntity<Map<String, Object>> validateInstrument(@RequestBody VfdValidationRequest request) {
        logInfo("Validating VFD instrument: " + request.getInstrumentCode());
        
        try {
            VfdValidationResponse response = vfdValidationService.validateInstrument(request);
            return successResponse("Instrument validation completed", "200", null, response);
        } catch (Exception e) {
            logError("Error validating instrument: " + e.getMessage());
            return errorResponse("Failed to validate instrument", "500", null, null);
        }
    }

    /**
     * Validate VFD broker
     */
    @PostMapping("/broker")
    @Operation(summary = "Validate Broker", description = "Validate VFD broker details")
    public ResponseEntity<Map<String, Object>> validateBroker(@RequestBody VfdValidationRequest request) {
        logInfo("Validating VFD broker: " + request.getBrokerCode());
        
        try {
            VfdValidationResponse response = vfdValidationService.validateBroker(request);
            return successResponse("Broker validation completed", "200", null, response);
        } catch (Exception e) {
            logError("Error validating broker: " + e.getMessage());
            return errorResponse("Failed to validate broker", "500", null, null);
        }
    }
}
