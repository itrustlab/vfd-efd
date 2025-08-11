package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdReconciliationRequest;
import tz.co.itrust.vfd.dto.VfdReconciliationResponse;
import tz.co.itrust.vfd.services.VfdReconciliationService;

import java.util.List;
import java.util.Map;

/**
 * VFD Reconciliation Controller
 * Handles VFD reconciliation processes and matching
 */
@RestController
@RequestMapping("/api/vfd/reconciliation")
@Tag(name = "VFD Reconciliation", description = "VFD reconciliation processes and matching")
@RequiredArgsConstructor
public class VfdReconciliationController extends BaseController {

    private final VfdReconciliationService vfdReconciliationService;

    /**
     * Initiate reconciliation process
     */
    @PostMapping("/initiate")
    @Operation(summary = "Initiate Reconciliation", description = "Initiate VFD reconciliation process")
    public ResponseEntity<Map<String, Object>> initiateReconciliation(@RequestBody VfdReconciliationRequest request) {
        logInfo("Initiating VFD reconciliation process: " + request.getReconciliationId());
        
        try {
            VfdReconciliationResponse response = vfdReconciliationService.initiateReconciliation(request);
            return successResponse("Reconciliation process initiated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error initiating reconciliation: " + e.getMessage());
            return errorResponse("Failed to initiate reconciliation", "500", null, null);
        }
    }

    /**
     * Process reconciliation
     */
    @PostMapping("/process")
    @Operation(summary = "Process Reconciliation", description = "Process VFD reconciliation")
    public ResponseEntity<Map<String, Object>> processReconciliation(@RequestBody VfdReconciliationRequest request) {
        logInfo("Processing VFD reconciliation: " + request.getReconciliationId());
        
        try {
            VfdReconciliationResponse response = vfdReconciliationService.processReconciliation(request);
            return successResponse("Reconciliation processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing reconciliation: " + e.getMessage());
            return errorResponse("Failed to process reconciliation", "500", null, null);
        }
    }

    /**
     * Get reconciliation status
     */
    @GetMapping("/status/{reconciliationId}")
    @Operation(summary = "Get Reconciliation Status", description = "Get VFD reconciliation status")
    public ResponseEntity<Map<String, Object>> getReconciliationStatus(@PathVariable String reconciliationId) {
        logInfo("Fetching reconciliation status: " + reconciliationId);
        
        try {
            VfdReconciliationResponse response = vfdReconciliationService.getReconciliationStatus(reconciliationId);
            return successResponse("Reconciliation status retrieved successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error fetching reconciliation status: " + e.getMessage());
            return errorResponse("Failed to fetch reconciliation status", "500", null, null);
        }
    }

    /**
     * Get reconciliation matches
     */
    @GetMapping("/matches/{reconciliationId}")
    @Operation(summary = "Get Reconciliation Matches", description = "Get VFD reconciliation matches")
    public ResponseEntity<Map<String, Object>> getReconciliationMatches(@PathVariable String reconciliationId) {
        logInfo("Fetching reconciliation matches: " + reconciliationId);
        
        try {
            List<VfdReconciliationResponse> matches = vfdReconciliationService.getReconciliationMatches(reconciliationId);
            return successResponse("Reconciliation matches retrieved successfully", "200", null, matches);
        } catch (Exception e) {
            logError("Error fetching reconciliation matches: " + e.getMessage());
            return errorResponse("Failed to fetch reconciliation matches", "500", null, null);
        }
    }

    /**
     * Get reconciliation breaks
     */
    @GetMapping("/breaks/{reconciliationId}")
    @Operation(summary = "Get Reconciliation Breaks", description = "Get VFD reconciliation breaks")
    public ResponseEntity<Map<String, Object>> getReconciliationBreaks(@PathVariable String reconciliationId) {
        logInfo("Fetching reconciliation breaks: " + reconciliationId);
        
        try {
            List<VfdReconciliationResponse> breaks = vfdReconciliationService.getReconciliationBreaks(reconciliationId);
            return successResponse("Reconciliation breaks retrieved successfully", "200", null, breaks);
        } catch (Exception e) {
            logError("Error fetching reconciliation breaks: " + e.getMessage());
            return errorResponse("Failed to fetch reconciliation breaks", "500", null, null);
        }
    }

    /**
     * Resolve reconciliation break
     */
    @PostMapping("/resolve-break")
    @Operation(summary = "Resolve Break", description = "Resolve a VFD reconciliation break")
    public ResponseEntity<Map<String, Object>> resolveReconciliationBreak(@RequestBody VfdReconciliationRequest request) {
        logInfo("Resolving reconciliation break: " + request.getBreakId());
        
        try {
            VfdReconciliationResponse response = vfdReconciliationService.resolveReconciliationBreak(request);
            return successResponse("Reconciliation break resolved successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error resolving reconciliation break: " + e.getMessage());
            return errorResponse("Failed to resolve reconciliation break", "500", null, null);
        }
    }

    /**
     * Get reconciliation history
     */
    @GetMapping("/history")
    @Operation(summary = "Get Reconciliation History", description = "Get VFD reconciliation history")
    public ResponseEntity<Map<String, Object>> getReconciliationHistory(
            @RequestParam(required = false) String reconciliationType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        logInfo("Fetching VFD reconciliation history");
        
        try {
            List<VfdReconciliationResponse> history = vfdReconciliationService.getReconciliationHistory(reconciliationType, status, startDate, endDate);
            return successResponse("Reconciliation history retrieved successfully", "200", null, history);
        } catch (Exception e) {
            logError("Error fetching reconciliation history: " + e.getMessage());
            return errorResponse("Failed to fetch reconciliation history", "500", null, null);
        }
    }

    /**
     * Get reconciliation statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get Reconciliation Statistics", description = "Get VFD reconciliation statistics")
    public ResponseEntity<Map<String, Object>> getReconciliationStatistics(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String reconciliationType) {
        logInfo("Fetching VFD reconciliation statistics");
        
        try {
            Map<String, Object> statistics = vfdReconciliationService.getReconciliationStatistics(period, reconciliationType);
            return successResponse("Reconciliation statistics retrieved successfully", "200", null, statistics);
        } catch (Exception e) {
            logError("Error fetching reconciliation statistics: " + e.getMessage());
            return errorResponse("Failed to fetch reconciliation statistics", "500", null, null);
        }
    }

    /**
     * Auto-reconcile transactions
     */
    @PostMapping("/auto-reconcile")
    @Operation(summary = "Auto Reconcile", description = "Auto-reconcile VFD transactions")
    public ResponseEntity<Map<String, Object>> autoReconcileTransactions(@RequestBody VfdReconciliationRequest request) {
        logInfo("Auto-reconciling VFD transactions");
        
        try {
            VfdReconciliationResponse response = vfdReconciliationService.autoReconcileTransactions(request);
            return successResponse("Auto-reconciliation completed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error auto-reconciling transactions: " + e.getMessage());
            return errorResponse("Failed to auto-reconcile transactions", "500", null, null);
        }
    }

    /**
     * Get reconciliation rules
     */
    @GetMapping("/rules")
    @Operation(summary = "Get Reconciliation Rules", description = "Get VFD reconciliation rules")
    public ResponseEntity<Map<String, Object>> getReconciliationRules() {
        logInfo("Fetching VFD reconciliation rules");
        
        try {
            VfdReconciliationResponse rules = vfdReconciliationService.getReconciliationRules();
            return successResponse("Reconciliation rules retrieved successfully", "200", null, rules);
        } catch (Exception e) {
            logError("Error fetching reconciliation rules: " + e.getMessage());
            return errorResponse("Failed to fetch reconciliation rules", "500", null, null);
        }
    }

    /**
     * Update reconciliation rules
     */
    @PutMapping("/rules")
    @Operation(summary = "Update Reconciliation Rules", description = "Update VFD reconciliation rules")
    public ResponseEntity<Map<String, Object>> updateReconciliationRules(@RequestBody VfdReconciliationRequest request) {
        logInfo("Updating VFD reconciliation rules");
        
        try {
            VfdReconciliationResponse response = vfdReconciliationService.updateReconciliationRules(request);
            return successResponse("Reconciliation rules updated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error updating reconciliation rules: " + e.getMessage());
            return errorResponse("Failed to update reconciliation rules", "500", null, null);
        }
    }
}
