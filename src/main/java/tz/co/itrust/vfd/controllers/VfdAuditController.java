package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdAuditRequest;
import tz.co.itrust.vfd.dto.VfdAuditResponse;
import tz.co.itrust.vfd.services.VfdAuditService;

import java.util.List;
import java.util.Map;

/**
 * VFD Audit Controller
 * Handles VFD audit trails and monitoring
 */
@RestController
@RequestMapping("/api/vfd/audit")
@Tag(name = "VFD Audit", description = "VFD audit trails and monitoring")
@RequiredArgsConstructor
public class VfdAuditController extends BaseController {

    private final VfdAuditService vfdAuditService;

    /**
     * Get audit trail
     */
    @GetMapping("/trail")
    @Operation(summary = "Get Audit Trail", description = "Get VFD audit trail")
    public ResponseEntity<Map<String, Object>> getAuditTrail(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String entityId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        logInfo("Fetching VFD audit trail");
        
        try {
            List<VfdAuditResponse> auditTrail = vfdAuditService.getAuditTrail(entityType, entityId, action, startDate, endDate);
            return successResponse("Audit trail retrieved successfully", "200", null, auditTrail);
        } catch (Exception e) {
            logError("Error fetching audit trail: " + e.getMessage());
            return errorResponse("Failed to fetch audit trail", "500", null, null);
        }
    }

    /**
     * Get transaction audit trail
     */
    @GetMapping("/transactions/{transactionId}")
    @Operation(summary = "Get Transaction Audit", description = "Get VFD transaction audit trail")
    public ResponseEntity<Map<String, Object>> getTransactionAuditTrail(@PathVariable String transactionId) {
        logInfo("Fetching VFD transaction audit trail: " + transactionId);
        
        try {
            List<VfdAuditResponse> auditTrail = vfdAuditService.getTransactionAuditTrail(transactionId);
            return successResponse("Transaction audit trail retrieved successfully", "200", null, auditTrail);
        } catch (Exception e) {
            logError("Error fetching transaction audit trail: " + e.getMessage());
            return errorResponse("Failed to fetch transaction audit trail", "500", null, null);
        }
    }

    /**
     * Get customer audit trail
     */
    @GetMapping("/customers/{customerId}")
    @Operation(summary = "Get Customer Audit", description = "Get VFD customer audit trail")
    public ResponseEntity<Map<String, Object>> getCustomerAuditTrail(@PathVariable String customerId) {
        logInfo("Fetching VFD customer audit trail: " + customerId);
        
        try {
            List<VfdAuditResponse> auditTrail = vfdAuditService.getCustomerAuditTrail(customerId);
            return successResponse("Customer audit trail retrieved successfully", "200", null, auditTrail);
        } catch (Exception e) {
            logError("Error fetching customer audit trail: " + e.getMessage());
            return errorResponse("Failed to fetch customer audit trail", "500", null, null);
        }
    }

    /**
     * Get user activity audit trail
     */
    @GetMapping("/users/{userId}")
    @Operation(summary = "Get User Activity Audit", description = "Get VFD user activity audit trail")
    public ResponseEntity<Map<String, Object>> getUserActivityAuditTrail(@PathVariable String userId) {
        logInfo("Fetching VFD user activity audit trail: " + userId);
        
        try {
            List<VfdAuditResponse> auditTrail = vfdAuditService.getUserActivityAuditTrail(userId);
            return successResponse("User activity audit trail retrieved successfully", "200", null, auditTrail);
        } catch (Exception e) {
            logError("Error fetching user activity audit trail: " + e.getMessage());
            return errorResponse("Failed to fetch user activity audit trail", "500", null, null);
        }
    }

    /**
     * Get system audit trail
     */
    @GetMapping("/system")
    @Operation(summary = "Get System Audit", description = "Get VFD system audit trail")
    public ResponseEntity<Map<String, Object>> getSystemAuditTrail(
            @RequestParam(required = false) String component,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        logInfo("Fetching VFD system audit trail");
        
        try {
            List<VfdAuditResponse> auditTrail = vfdAuditService.getSystemAuditTrail(component, severity, startDate, endDate);
            return successResponse("System audit trail retrieved successfully", "200", null, auditTrail);
        } catch (Exception e) {
            logError("Error fetching system audit trail: " + e.getMessage());
            return errorResponse("Failed to fetch system audit trail", "500", null, null);
        }
    }

    /**
     * Get compliance audit trail
     */
    @GetMapping("/compliance")
    @Operation(summary = "Get Compliance Audit", description = "Get VFD compliance audit trail")
    public ResponseEntity<Map<String, Object>> getComplianceAuditTrail(
            @RequestParam(required = false) String complianceType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        logInfo("Fetching VFD compliance audit trail");
        
        try {
            List<VfdAuditResponse> auditTrail = vfdAuditService.getComplianceAuditTrail(complianceType, status, startDate, endDate);
            return successResponse("Compliance audit trail retrieved successfully", "200", null, auditTrail);
        } catch (Exception e) {
            logError("Error fetching compliance audit trail: " + e.getMessage());
            return errorResponse("Failed to fetch compliance audit trail", "500", null, null);
        }
    }

    /**
     * Export audit trail
     */
    @PostMapping("/export")
    @Operation(summary = "Export Audit Trail", description = "Export VFD audit trail")
    public ResponseEntity<Map<String, Object>> exportAuditTrail(@RequestBody VfdAuditRequest request) {
        logInfo("Exporting VFD audit trail");
        
        try {
            VfdAuditResponse response = vfdAuditService.exportAuditTrail(request);
            return successResponse("Audit trail exported successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error exporting audit trail: " + e.getMessage());
            return errorResponse("Failed to export audit trail", "500", null, null);
        }
    }

    /**
     * Get audit statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get Audit Statistics", description = "Get VFD audit statistics")
    public ResponseEntity<Map<String, Object>> getAuditStatistics(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String entityType) {
        logInfo("Fetching VFD audit statistics");
        
        try {
            Map<String, Object> statistics = vfdAuditService.getAuditStatistics(period, entityType);
            return successResponse("Audit statistics retrieved successfully", "200", null, statistics);
        } catch (Exception e) {
            logError("Error fetching audit statistics: " + e.getMessage());
            return errorResponse("Failed to fetch audit statistics", "500", null, null);
        }
    }

    /**
     * Get audit dashboard
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get Audit Dashboard", description = "Get VFD audit dashboard")
    public ResponseEntity<Map<String, Object>> getAuditDashboard() {
        logInfo("Fetching VFD audit dashboard");
        
        try {
            VfdAuditResponse dashboard = vfdAuditService.getAuditDashboard();
            return successResponse("Audit dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching audit dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch audit dashboard", "500", null, null);
        }
    }

    /**
     * Update audit settings
     */
    @PutMapping("/settings")
    @Operation(summary = "Update Audit Settings", description = "Update VFD audit settings")
    public ResponseEntity<Map<String, Object>> updateAuditSettings(@RequestBody VfdAuditRequest request) {
        logInfo("Updating VFD audit settings");
        
        try {
            VfdAuditResponse response = vfdAuditService.updateAuditSettings(request);
            return successResponse("Audit settings updated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error updating audit settings: " + e.getMessage());
            return errorResponse("Failed to update audit settings", "500", null, null);
        }
    }

    /**
     * Get audit settings
     */
    @GetMapping("/settings")
    @Operation(summary = "Get Audit Settings", description = "Get VFD audit settings")
    public ResponseEntity<Map<String, Object>> getAuditSettings() {
        logInfo("Fetching VFD audit settings");
        
        try {
            VfdAuditResponse settings = vfdAuditService.getAuditSettings();
            return successResponse("Audit settings retrieved successfully", "200", null, settings);
        } catch (Exception e) {
            logError("Error fetching audit settings: " + e.getMessage());
            return errorResponse("Failed to fetch audit settings", "500", null, null);
        }
    }
}
