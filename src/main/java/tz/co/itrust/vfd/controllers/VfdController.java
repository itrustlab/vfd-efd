package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdTransactionRequest;
import tz.co.itrust.vfd.dto.VfdTransactionResponse;
import tz.co.itrust.vfd.services.VfdTransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * VFD Controller
 * Handles VFD-related API endpoints
 * 
 * This controller provides the main VFD operations including:
 * - Health checks and service information
 * - Transaction management
 * - Service status monitoring
 * - Basic VFD processing
 */
@RestController
@RequestMapping("/api/vfd")
@Tag(name = "VFD Operations", description = "Virtual Financial Data operations")
@RequiredArgsConstructor
public class VfdController extends BaseController {

    private final VfdTransactionService vfdTransactionService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Check if VFD service is running")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logInfo("Health check requested");
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("service", "itrust-vfd");
        data.put("timestamp", java.time.LocalDateTime.now().toString());
        
        return successResponse("VFD Service is running", "200", null, data);
    }

    /**
     * Get VFD information
     */
    @GetMapping("/info")
    @Operation(summary = "Get VFD Info", description = "Get VFD service information")
    public ResponseEntity<Map<String, Object>> getVfdInfo() {
        logInfo("VFD info requested");
        Map<String, Object> data = new HashMap<>();
        data.put("service", "itrust-vfd");
        data.put("version", "1.0.0");
        data.put("description", "Virtual Financial Data Microservice");
        data.put("endpoints", new String[]{
            "/api/vfd/health",
            "/api/vfd/info",
            "/api/vfd/status",
            "/api/vfd/process",
            "/api/vfd/transactions",
            "/api/vfd/integration/*",
            "/api/vfd/validation/*",
            "/api/vfd/reporting/*",
            "/api/vfd/settlement/*",
            "/api/vfd/compliance/*",
            "/api/vfd/webhook/*",
            "/api/vfd/batch/*",
            "/api/vfd/reconciliation/*",
            "/api/vfd/audit/*",
            "/api/vfd/dashboard/*"
        });
        
        return successResponse("VFD service information retrieved", "200", null, data);
    }

    /**
     * Process VFD request
     */
    @PostMapping("/process")
    @Operation(summary = "Process VFD Request", description = "Process a VFD-related request")
    public ResponseEntity<Map<String, Object>> processVfdRequest(@RequestBody Map<String, Object> request) {
        logInfo("VFD processing request received: " + request);
        
        try {
            // Process VFD request based on the type and content
            Map<String, Object> data = new HashMap<>();
            data.put("requestId", java.util.UUID.randomUUID().toString());
            data.put("status", "PROCESSING");
            data.put("message", "VFD request received and queued for processing");
            data.put("requestType", request.get("type"));
            data.put("timestamp", java.time.LocalDateTime.now().toString());
            
            return successResponse("VFD request processed successfully", "200", null, data);
            
        } catch (Exception e) {
            return exceptionResponse(e, "500");
        }
    }

    /**
     * Get VFD status
     */
    @GetMapping("/status")
    @Operation(summary = "Get VFD Status", description = "Get current VFD service status")
    public ResponseEntity<Map<String, Object>> getVfdStatus() {
        logInfo("VFD status requested");
        Map<String, Object> data = new HashMap<>();
        data.put("service", "itrust-vfd");
        data.put("status", "ACTIVE");
        data.put("uptime", "Running");
        data.put("lastCheck", java.time.LocalDateTime.now().toString());
        data.put("activeControllers", new String[]{
            "VfdController",
            "VfdApiIntegrationController", 
            "VfdValidationController",
            "VfdReportingController",
            "VfdSettlementController",
            "VfdComplianceController",
            "VfdWebhookController",
            "VfdBatchProcessingController",
            "VfdReconciliationController",
            "VfdAuditController",
            "VfdDashboardController"
        });
        
        return successResponse("VFD status retrieved", "200", null, data);
    }

    /**
     * Create VFD transaction
     */
    @PostMapping("/transactions")
    @Operation(summary = "Create VFD Transaction", description = "Create a new VFD transaction")
    public ResponseEntity<Map<String, Object>> createTransaction(@RequestBody VfdTransactionRequest request) {
        logInfo("Creating VFD transaction for customer: " + request.getCustomerId());
        
        try {
            VfdTransactionResponse response = vfdTransactionService.createTransaction(request);
            return successResponse("VFD transaction created successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error creating VFD transaction: " + e.getMessage());
            return errorResponse("Failed to create VFD transaction", "500", null, null);
        }
    }

    /**
     * Get VFD transaction by ID
     */
    @GetMapping("/transactions/{transactionId}")
    @Operation(summary = "Get VFD Transaction", description = "Get VFD transaction by ID")
    public ResponseEntity<Map<String, Object>> getTransaction(@PathVariable String transactionId) {
        logInfo("Fetching VFD transaction: " + transactionId);
        
        try {
            Optional<VfdTransactionResponse> transaction = vfdTransactionService.getTransactionById(transactionId);
            if (transaction.isPresent()) {
                return successResponse("VFD transaction retrieved successfully", "200", null, transaction.get());
            } else {
                return errorResponse("Transaction not found", "404", null, null);
            }
        } catch (Exception e) {
            logError("Error fetching VFD transaction: " + e.getMessage());
            return errorResponse("Failed to fetch VFD transaction", "500", null, null);
        }
    }

    /**
     * Get VFD transactions by customer ID
     */
    @GetMapping("/customers/{customerId}/transactions")
    @Operation(summary = "Get Customer Transactions", description = "Get all VFD transactions for a customer")
    public ResponseEntity<Map<String, Object>> getCustomerTransactions(@PathVariable String customerId) {
        logInfo("Fetching VFD transactions for customer: " + customerId);
        
        try {
            List<VfdTransactionResponse> transactions = vfdTransactionService.getTransactionsByCustomerId(customerId);
            return successResponse("Customer transactions retrieved successfully", "200", null, transactions);
        } catch (Exception e) {
            logError("Error fetching customer transactions: " + e.getMessage());
            return errorResponse("Failed to fetch customer transactions", "500", null, null);
        }
    }

    /**
     * Get VFD transaction statistics
     */
    @GetMapping("/customers/{customerId}/statistics")
    @Operation(summary = "Get Transaction Statistics", description = "Get VFD transaction statistics for a customer")
    public ResponseEntity<Map<String, Object>> getTransactionStatistics(@PathVariable String customerId) {
        logInfo("Fetching VFD transaction statistics for customer: " + customerId);
        
        try {
            VfdTransactionService.VfdTransactionStatistics statistics = vfdTransactionService.getTransactionStatistics(customerId);
            return successResponse("Transaction statistics retrieved successfully", "200", null, statistics);
        } catch (Exception e) {
            logError("Error fetching transaction statistics: " + e.getMessage());
            return errorResponse("Failed to fetch transaction statistics", "500", null, null);
        }
    }

    /**
     * Update VFD transaction status
     */
    @PutMapping("/transactions/{transactionId}/status")
    @Operation(summary = "Update Transaction Status", description = "Update VFD transaction status")
    public ResponseEntity<Map<String, Object>> updateTransactionStatus(
            @PathVariable String transactionId,
            @RequestParam String status) {
        logInfo("Updating VFD transaction status: " + transactionId + " to " + status);
        
        try {
            VfdTransactionResponse response = vfdTransactionService.updateTransactionStatus(transactionId, status);
            return successResponse("Transaction status updated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error updating transaction status: " + e.getMessage());
            return errorResponse("Failed to update transaction status", "500", null, null);
        }
    }

    /**
     * Get VFD service capabilities
     */
    @GetMapping("/capabilities")
    @Operation(summary = "Get Service Capabilities", description = "Get VFD service capabilities and features")
    public ResponseEntity<Map<String, Object>> getServiceCapabilities() {
        logInfo("VFD service capabilities requested");
        Map<String, Object> data = new HashMap<>();
        data.put("service", "itrust-vfd");
        data.put("capabilities", new String[]{
            "Transaction Management",
            "Settlement Processing", 
            "Compliance Monitoring",
            "Regulatory Reporting",
            "Audit Trail Management",
            "Real-time Monitoring",
            "Batch Processing",
            "Reconciliation",
            "Webhook Integration",
            "API Integration",
            "Dashboard Analytics",
            "Risk Management"
        });
        data.put("regulatoryCompliance", new String[]{
            "VFD Regulations",
            "Capital Markets Authority",
            "Bank of Tanzania",
            "International Standards"
        });
        
        return successResponse("VFD service capabilities retrieved", "200", null, data);
    }
} 