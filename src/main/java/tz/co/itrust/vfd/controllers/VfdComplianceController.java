package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdComplianceRequest;
import tz.co.itrust.vfd.dto.VfdComplianceResponse;
import tz.co.itrust.vfd.services.VfdComplianceService;

import java.util.List;
import java.util.Map;

/**
 * VFD Compliance Controller
 * Handles VFD regulatory compliance and monitoring
 */
@RestController
@RequestMapping("/api/vfd/compliance")
@Tag(name = "VFD Compliance", description = "VFD regulatory compliance and monitoring")
@RequiredArgsConstructor
public class VfdComplianceController extends BaseController {

    private final VfdComplianceService vfdComplianceService;

    /**
     * Check transaction compliance
     */
    @PostMapping("/check-transaction")
    @Operation(summary = "Check Transaction Compliance", description = "Check compliance for a VFD transaction")
    public ResponseEntity<Map<String, Object>> checkTransactionCompliance(@RequestBody VfdComplianceRequest request) {
        logInfo("Checking compliance for transaction: " + request.getTransactionId());
        
        try {
            VfdComplianceResponse response = vfdComplianceService.checkTransactionCompliance(request);
            return successResponse("Transaction compliance check completed", "200", null, response);
        } catch (Exception e) {
            logError("Error checking transaction compliance: " + e.getMessage());
            return errorResponse("Failed to check transaction compliance", "500", null, null);
        }
    }

    /**
     * Check customer compliance
     */
    @PostMapping("/check-customer")
    @Operation(summary = "Check Customer Compliance", description = "Check compliance for a VFD customer")
    public ResponseEntity<Map<String, Object>> checkCustomerCompliance(@RequestBody VfdComplianceRequest request) {
        logInfo("Checking compliance for customer: " + request.getCustomerId());
        
        try {
            VfdComplianceResponse response = vfdComplianceService.checkCustomerCompliance(request);
            return successResponse("Customer compliance check completed", "200", null, response);
        } catch (Exception e) {
            logError("Error checking customer compliance: " + e.getMessage());
            return errorResponse("Failed to check customer compliance", "500", null, null);
        }
    }

    /**
     * Get compliance rules
     */
    @GetMapping("/rules")
    @Operation(summary = "Get Compliance Rules", description = "Get VFD compliance rules and regulations")
    public ResponseEntity<Map<String, Object>> getComplianceRules() {
        logInfo("Fetching VFD compliance rules");
        
        try {
            Map<String, Object> rules = vfdComplianceService.getComplianceRules();
            return successResponse("Compliance rules retrieved successfully", "200", null, rules);
        } catch (Exception e) {
            logError("Error fetching compliance rules: " + e.getMessage());
            return errorResponse("Failed to fetch compliance rules", "500", null, null);
        }
    }

    /**
     * Get compliance violations
     */
    @GetMapping("/violations")
    @Operation(summary = "Get Compliance Violations", description = "Get VFD compliance violations")
    public ResponseEntity<Map<String, Object>> getComplianceViolations(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status) {
        logInfo("Fetching VFD compliance violations");
        
        try {
            List<VfdComplianceResponse> violations = vfdComplianceService.getComplianceViolations(customerId, severity, status);
            return successResponse("Compliance violations retrieved successfully", "200", null, violations);
        } catch (Exception e) {
            logError("Error fetching compliance violations: " + e.getMessage());
            return errorResponse("Failed to fetch compliance violations", "500", null, null);
        }
    }

    /**
     * Resolve compliance violation
     */
    @PostMapping("/resolve-violation")
    @Operation(summary = "Resolve Violation", description = "Resolve a VFD compliance violation")
    public ResponseEntity<Map<String, Object>> resolveComplianceViolation(@RequestBody VfdComplianceRequest request) {
        logInfo("Resolving compliance violation: " + request.getViolationId());
        
        try {
            VfdComplianceResponse response = vfdComplianceService.resolveComplianceViolation(request);
            return successResponse("Compliance violation resolved successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error resolving compliance violation: " + e.getMessage());
            return errorResponse("Failed to resolve compliance violation", "500", null, null);
        }
    }

    /**
     * Get compliance dashboard
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get Compliance Dashboard", description = "Get VFD compliance dashboard data")
    public ResponseEntity<Map<String, Object>> getComplianceDashboard() {
        logInfo("Fetching VFD compliance dashboard");
        
        try {
            Map<String, Object> dashboard = vfdComplianceService.getComplianceDashboard();
            return successResponse("Compliance dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching compliance dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch compliance dashboard", "500", null, null);
        }
    }

    /**
     * Get compliance audit trail
     */
    @GetMapping("/audit-trail")
    @Operation(summary = "Get Audit Trail", description = "Get VFD compliance audit trail")
    public ResponseEntity<Map<String, Object>> getComplianceAuditTrail(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        logInfo("Fetching VFD compliance audit trail");
        
        try {
            List<VfdComplianceResponse> auditTrail = vfdComplianceService.getComplianceAuditTrail(customerId, startDate, endDate);
            return successResponse("Compliance audit trail retrieved successfully", "200", null, auditTrail);
        } catch (Exception e) {
            logError("Error fetching compliance audit trail: " + e.getMessage());
            return errorResponse("Failed to fetch compliance audit trail", "500", null, null);
        }
    }

    /**
     * Update compliance rules
     */
    @PutMapping("/rules")
    @Operation(summary = "Update Compliance Rules", description = "Update VFD compliance rules")
    public ResponseEntity<Map<String, Object>> updateComplianceRules(@RequestBody VfdComplianceRequest request) {
        logInfo("Updating VFD compliance rules");
        
        try {
            VfdComplianceResponse response = vfdComplianceService.updateComplianceRules(request);
            return successResponse("Compliance rules updated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error updating compliance rules: " + e.getMessage());
            return errorResponse("Failed to update compliance rules", "500", null, null);
        }
    }

    /**
     * Get compliance metrics
     */
    @GetMapping("/metrics")
    @Operation(summary = "Get Compliance Metrics", description = "Get VFD compliance metrics")
    public ResponseEntity<Map<String, Object>> getComplianceMetrics(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String customerId) {
        logInfo("Fetching VFD compliance metrics");
        
        try {
            Map<String, Object> metrics = vfdComplianceService.getComplianceMetrics(period, customerId);
            return successResponse("Compliance metrics retrieved successfully", "200", null, metrics);
        } catch (Exception e) {
            logError("Error fetching compliance metrics: " + e.getMessage());
            return errorResponse("Failed to fetch compliance metrics", "500", null, null);
        }
    }
}
