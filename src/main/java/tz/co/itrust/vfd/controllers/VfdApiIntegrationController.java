package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdExternalApiRequest;
import tz.co.itrust.vfd.dto.VfdExternalApiResponse;
import tz.co.itrust.vfd.services.VfdApiIntegrationService;

import java.util.Map;

/**
 * VFD API Integration Controller
 * Handles external VFD system integrations and API calls
 */
@RestController
@RequestMapping("/api/vfd/integration")
@Tag(name = "VFD API Integration", description = "External VFD system integrations and API calls")
@RequiredArgsConstructor
public class VfdApiIntegrationController extends BaseController {

    private final VfdApiIntegrationService vfdApiIntegrationService;

    /**
     * Send transaction to external VFD system
     */
    @PostMapping("/send-transaction")
    @Operation(summary = "Send Transaction to VFD", description = "Send a transaction to the external VFD system")
    public ResponseEntity<Map<String, Object>> sendTransactionToVfd(@RequestBody VfdExternalApiRequest request) {
        logInfo("Sending transaction to external VFD system: " + request.getTransactionId());
        
        try {
            VfdExternalApiResponse response = vfdApiIntegrationService.sendTransactionToVfd(request);
            return successResponse("Transaction sent to VFD system successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error sending transaction to VFD system: " + e.getMessage());
            return errorResponse("Failed to send transaction to VFD system", "500", null, null);
        }
    }

    /**
     * Get transaction status from external VFD system
     */
    @GetMapping("/transaction-status/{vfdReference}")
    @Operation(summary = "Get VFD Transaction Status", description = "Get transaction status from external VFD system")
    public ResponseEntity<Map<String, Object>> getVfdTransactionStatus(@PathVariable String vfdReference) {
        logInfo("Fetching VFD transaction status: " + vfdReference);
        
        try {
            VfdExternalApiResponse response = vfdApiIntegrationService.getTransactionStatus(vfdReference);
            return successResponse("VFD transaction status retrieved successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error fetching VFD transaction status: " + e.getMessage());
            return errorResponse("Failed to fetch VFD transaction status", "500", null, null);
        }
    }

    /**
     * Sync transactions with external VFD system
     */
    @PostMapping("/sync-transactions")
    @Operation(summary = "Sync VFD Transactions", description = "Synchronize transactions with external VFD system")
    public ResponseEntity<Map<String, Object>> syncVfdTransactions(@RequestBody Map<String, Object> request) {
        logInfo("Syncing VFD transactions with external system");
        
        try {
            Map<String, Object> response = vfdApiIntegrationService.syncTransactions(request);
            return successResponse("VFD transactions synchronized successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error syncing VFD transactions: " + e.getMessage());
            return errorResponse("Failed to sync VFD transactions", "500", null, null);
        }
    }

    /**
     * Test VFD API connectivity
     */
    @GetMapping("/test-connection")
    @Operation(summary = "Test VFD API Connection", description = "Test connectivity to external VFD system")
    public ResponseEntity<Map<String, Object>> testVfdApiConnection() {
        logInfo("Testing VFD API connection");
        
        try {
            Map<String, Object> response = vfdApiIntegrationService.testConnection();
            return successResponse("VFD API connection test completed", "200", null, response);
        } catch (Exception e) {
            logError("Error testing VFD API connection: " + e.getMessage());
            return errorResponse("Failed to test VFD API connection", "500", null, null);
        }
    }

    /**
     * Get VFD API configuration
     */
    @GetMapping("/config")
    @Operation(summary = "Get VFD API Config", description = "Get VFD API configuration details")
    public ResponseEntity<Map<String, Object>> getVfdApiConfig() {
        logInfo("Fetching VFD API configuration");
        
        try {
            Map<String, Object> config = vfdApiIntegrationService.getApiConfiguration();
            return successResponse("VFD API configuration retrieved successfully", "200", null, config);
        } catch (Exception e) {
            logError("Error fetching VFD API configuration: " + e.getMessage());
            return errorResponse("Failed to fetch VFD API configuration", "500", null, null);
        }
    }
}
