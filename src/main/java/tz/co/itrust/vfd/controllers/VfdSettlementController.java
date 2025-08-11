package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdSettlementRequest;
import tz.co.itrust.vfd.dto.VfdSettlementResponse;
import tz.co.itrust.vfd.services.VfdSettlementService;

import java.util.List;
import java.util.Map;

/**
 * VFD Settlement Controller
 * Handles VFD settlement operations and processes
 */
@RestController
@RequestMapping("/api/vfd/settlement")
@Tag(name = "VFD Settlement", description = "VFD settlement operations and processes")
@RequiredArgsConstructor
public class VfdSettlementController extends BaseController {

    private final VfdSettlementService vfdSettlementService;

    /**
     * Initiate settlement for a transaction
     */
    @PostMapping("/initiate")
    @Operation(summary = "Initiate Settlement", description = "Initiate settlement for a VFD transaction")
    public ResponseEntity<Map<String, Object>> initiateSettlement(@RequestBody VfdSettlementRequest request) {
        logInfo("Initiating settlement for transaction: " + request.getTransactionId());
        
        try {
            VfdSettlementResponse response = vfdSettlementService.initiateSettlement(request);
            return successResponse("Settlement initiated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error initiating settlement: " + e.getMessage());
            return errorResponse("Failed to initiate settlement", "500", null, null);
        }
    }

    /**
     * Process settlement for a transaction
     */
    @PostMapping("/process")
    @Operation(summary = "Process Settlement", description = "Process settlement for a VFD transaction")
    public ResponseEntity<Map<String, Object>> processSettlement(@RequestBody VfdSettlementRequest request) {
        logInfo("Processing settlement for transaction: " + request.getTransactionId());
        
        try {
            VfdSettlementResponse response = vfdSettlementService.processSettlement(request);
            return successResponse("Settlement processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing settlement: " + e.getMessage());
            return errorResponse("Failed to process settlement", "500", null, null);
        }
    }

    /**
     * Confirm settlement completion
     */
    @PostMapping("/confirm")
    @Operation(summary = "Confirm Settlement", description = "Confirm settlement completion for a VFD transaction")
    public ResponseEntity<Map<String, Object>> confirmSettlement(@RequestBody VfdSettlementRequest request) {
        logInfo("Confirming settlement for transaction: " + request.getTransactionId());
        
        try {
            VfdSettlementResponse response = vfdSettlementService.confirmSettlement(request);
            return successResponse("Settlement confirmed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error confirming settlement: " + e.getMessage());
            return errorResponse("Failed to confirm settlement", "500", null, null);
        }
    }

    /**
     * Get settlement status
     */
    @GetMapping("/status/{transactionId}")
    @Operation(summary = "Get Settlement Status", description = "Get settlement status for a VFD transaction")
    public ResponseEntity<Map<String, Object>> getSettlementStatus(@PathVariable String transactionId) {
        logInfo("Fetching settlement status for transaction: " + transactionId);
        
        try {
            VfdSettlementResponse response = vfdSettlementService.getSettlementStatus(transactionId);
            return successResponse("Settlement status retrieved successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error fetching settlement status: " + e.getMessage());
            return errorResponse("Failed to fetch settlement status", "500", null, null);
        }
    }

    /**
     * Get pending settlements
     */
    @GetMapping("/pending")
    @Operation(summary = "Get Pending Settlements", description = "Get list of pending VFD settlements")
    public ResponseEntity<Map<String, Object>> getPendingSettlements() {
        logInfo("Fetching pending VFD settlements");
        
        try {
            List<VfdSettlementResponse> settlements = vfdSettlementService.getPendingSettlements();
            return successResponse("Pending settlements retrieved successfully", "200", null, settlements);
        } catch (Exception e) {
            logError("Error fetching pending settlements: " + e.getMessage());
            return errorResponse("Failed to fetch pending settlements", "500", null, null);
        }
    }

    /**
     * Get settlement history
     */
    @GetMapping("/history")
    @Operation(summary = "Get Settlement History", description = "Get VFD settlement history")
    public ResponseEntity<Map<String, Object>> getSettlementHistory(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        logInfo("Fetching VFD settlement history");
        
        try {
            List<VfdSettlementResponse> settlements = vfdSettlementService.getSettlementHistory(customerId, startDate, endDate);
            return successResponse("Settlement history retrieved successfully", "200", null, settlements);
        } catch (Exception e) {
            logError("Error fetching settlement history: " + e.getMessage());
            return errorResponse("Failed to fetch settlement history", "500", null, null);
        }
    }

    /**
     * Cancel settlement
     */
    @PostMapping("/cancel")
    @Operation(summary = "Cancel Settlement", description = "Cancel a pending VFD settlement")
    public ResponseEntity<Map<String, Object>> cancelSettlement(@RequestBody VfdSettlementRequest request) {
        logInfo("Cancelling settlement for transaction: " + request.getTransactionId());
        
        try {
            VfdSettlementResponse response = vfdSettlementService.cancelSettlement(request);
            return successResponse("Settlement cancelled successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error cancelling settlement: " + e.getMessage());
            return errorResponse("Failed to cancel settlement", "500", null, null);
        }
    }

    /**
     * Get settlement statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get Settlement Statistics", description = "Get VFD settlement statistics")
    public ResponseEntity<Map<String, Object>> getSettlementStatistics(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String period) {
        logInfo("Fetching VFD settlement statistics");
        
        try {
            Map<String, Object> statistics = vfdSettlementService.getSettlementStatistics(customerId, period);
            return successResponse("Settlement statistics retrieved successfully", "200", null, statistics);
        } catch (Exception e) {
            logError("Error fetching settlement statistics: " + e.getMessage());
            return errorResponse("Failed to fetch settlement statistics", "500", null, null);
        }
    }
}
