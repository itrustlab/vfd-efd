package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdWebhookRequest;
import tz.co.itrust.vfd.dto.VfdWebhookResponse;
import tz.co.itrust.vfd.services.VfdWebhookService;

import java.util.Map;

/**
 * VFD Webhook Controller
 * Handles incoming VFD notifications and webhooks
 */
@RestController
@RequestMapping("/api/vfd/webhook")
@Tag(name = "VFD Webhook", description = "VFD webhook notifications and callbacks")
@RequiredArgsConstructor
public class VfdWebhookController extends BaseController {

    private final VfdWebhookService vfdWebhookService;

    /**
     * Receive VFD transaction notification
     */
    @PostMapping("/transaction-notification")
    @Operation(summary = "Receive Transaction Notification", description = "Receive VFD transaction notification")
    public ResponseEntity<Map<String, Object>> receiveTransactionNotification(@RequestBody VfdWebhookRequest request) {
        logInfo("Received VFD transaction notification: " + request.getTransactionId());
        
        try {
            VfdWebhookResponse response = vfdWebhookService.processTransactionNotification(request);
            return successResponse("Transaction notification processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing transaction notification: " + e.getMessage());
            return errorResponse("Failed to process transaction notification", "500", null, null);
        }
    }

    /**
     * Receive VFD settlement notification
     */
    @PostMapping("/settlement-notification")
    @Operation(summary = "Receive Settlement Notification", description = "Receive VFD settlement notification")
    public ResponseEntity<Map<String, Object>> receiveSettlementNotification(@RequestBody VfdWebhookRequest request) {
        logInfo("Received VFD settlement notification: " + request.getSettlementId());
        
        try {
            VfdWebhookResponse response = vfdWebhookService.processSettlementNotification(request);
            return successResponse("Settlement notification processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing settlement notification: " + e.getMessage());
            return errorResponse("Failed to process settlement notification", "500", null, null);
        }
    }

    /**
     * Receive VFD compliance alert
     */
    @PostMapping("/compliance-alert")
    @Operation(summary = "Receive Compliance Alert", description = "Receive VFD compliance alert")
    public ResponseEntity<Map<String, Object>> receiveComplianceAlert(@RequestBody VfdWebhookRequest request) {
        logInfo("Received VFD compliance alert: " + request.getAlertId());
        
        try {
            VfdWebhookResponse response = vfdWebhookService.processComplianceAlert(request);
            return successResponse("Compliance alert processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing compliance alert: " + e.getMessage());
            return errorResponse("Failed to process compliance alert", "500", null, null);
        }
    }

    /**
     * Receive VFD system status update
     */
    @PostMapping("/system-status")
    @Operation(summary = "Receive System Status", description = "Receive VFD system status update")
    public ResponseEntity<Map<String, Object>> receiveSystemStatusUpdate(@RequestBody VfdWebhookRequest request) {
        logInfo("Received VFD system status update");
        
        try {
            VfdWebhookResponse response = vfdWebhookService.processSystemStatusUpdate(request);
            return successResponse("System status update processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing system status update: " + e.getMessage());
            return errorResponse("Failed to process system status update", "500", null, null);
        }
    }

    /**
     * Receive VFD market data update
     */
    @PostMapping("/market-data")
    @Operation(summary = "Receive Market Data", description = "Receive VFD market data update")
    public ResponseEntity<Map<String, Object>> receiveMarketDataUpdate(@RequestBody VfdWebhookRequest request) {
        logInfo("Received VFD market data update for instrument: " + request.getInstrumentCode());
        
        try {
            VfdWebhookResponse response = vfdWebhookService.processMarketDataUpdate(request);
            return successResponse("Market data update processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing market data update: " + e.getMessage());
            return errorResponse("Failed to process market data update", "500", null, null);
        }
    }

    /**
     * Receive VFD regulatory update
     */
    @PostMapping("/regulatory-update")
    @Operation(summary = "Receive Regulatory Update", description = "Receive VFD regulatory update")
    public ResponseEntity<Map<String, Object>> receiveRegulatoryUpdate(@RequestBody VfdWebhookRequest request) {
        logInfo("Received VFD regulatory update: " + request.getRegulatoryId());
        
        try {
            VfdWebhookResponse response = vfdWebhookService.processRegulatoryUpdate(request);
            return successResponse("Regulatory update processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing regulatory update: " + e.getMessage());
            return errorResponse("Failed to process regulatory update", "500", null, null);
        }
    }

    /**
     * Get webhook configuration
     */
    @GetMapping("/config")
    @Operation(summary = "Get Webhook Config", description = "Get VFD webhook configuration")
    public ResponseEntity<Map<String, Object>> getWebhookConfiguration() {
        logInfo("Fetching VFD webhook configuration");
        
        try {
            Map<String, Object> config = vfdWebhookService.getWebhookConfiguration();
            return successResponse("Webhook configuration retrieved successfully", "200", null, config);
        } catch (Exception e) {
            logError("Error fetching webhook configuration: " + e.getMessage());
            return errorResponse("Failed to fetch webhook configuration", "500", null, null);
        }
    }

    /**
     * Test webhook endpoint
     */
    @PostMapping("/test")
    @Operation(summary = "Test Webhook", description = "Test VFD webhook endpoint")
    public ResponseEntity<Map<String, Object>> testWebhook(@RequestBody VfdWebhookRequest request) {
        logInfo("Testing VFD webhook endpoint");
        
        try {
            VfdWebhookResponse response = vfdWebhookService.testWebhook(request);
            return successResponse("Webhook test completed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error testing webhook: " + e.getMessage());
            return errorResponse("Failed to test webhook", "500", null, null);
        }
    }

    /**
     * Get webhook history
     */
    @GetMapping("/history")
    @Operation(summary = "Get Webhook History", description = "Get VFD webhook history")
    public ResponseEntity<Map<String, Object>> getWebhookHistory(
            @RequestParam(required = false) String webhookType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        logInfo("Fetching VFD webhook history");
        
        try {
            Map<String, Object> history = vfdWebhookService.getWebhookHistory(webhookType, status, startDate, endDate);
            return successResponse("Webhook history retrieved successfully", "200", null, history);
        } catch (Exception e) {
            logError("Error fetching webhook history: " + e.getMessage());
            return errorResponse("Failed to fetch webhook history", "500", null, null);
        }
    }
}
