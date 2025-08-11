package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdBatchRequest;
import tz.co.itrust.vfd.dto.VfdBatchResponse;
import tz.co.itrust.vfd.services.VfdBatchProcessingService;

import java.util.List;
import java.util.Map;

/**
 * VFD Batch Processing Controller
 * Handles VFD batch operations and bulk processing
 */
@RestController
@RequestMapping("/api/vfd/batch")
@Tag(name = "VFD Batch Processing", description = "VFD batch operations and bulk processing")
@RequiredArgsConstructor
public class VfdBatchProcessingController extends BaseController {

    private final VfdBatchProcessingService vfdBatchProcessingService;

    /**
     * Process batch transactions
     */
    @PostMapping("/transactions")
    @Operation(summary = "Process Batch Transactions", description = "Process multiple VFD transactions in batch")
    public ResponseEntity<Map<String, Object>> processBatchTransactions(@RequestBody VfdBatchRequest request) {
        logInfo("Processing batch transactions: " + request.getBatchId());
        
        try {
            VfdBatchResponse response = vfdBatchProcessingService.processBatchTransactions(request);
            return successResponse("Batch transactions processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing batch transactions: " + e.getMessage());
            return errorResponse("Failed to process batch transactions", "500", null, null);
        }
    }

    /**
     * Process batch settlements
     */
    @PostMapping("/settlements")
    @Operation(summary = "Process Batch Settlements", description = "Process multiple VFD settlements in batch")
    public ResponseEntity<Map<String, Object>> processBatchSettlements(@RequestBody VfdBatchRequest request) {
        logInfo("Processing batch settlements: " + request.getBatchId());
        
        try {
            VfdBatchResponse response = vfdBatchProcessingService.processBatchSettlements(request);
            return successResponse("Batch settlements processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing batch settlements: " + e.getMessage());
            return errorResponse("Failed to process batch settlements", "500", null, null);
        }
    }

    /**
     * Process batch compliance checks
     */
    @PostMapping("/compliance-checks")
    @Operation(summary = "Process Batch Compliance", description = "Process multiple VFD compliance checks in batch")
    public ResponseEntity<Map<String, Object>> processBatchComplianceChecks(@RequestBody VfdBatchRequest request) {
        logInfo("Processing batch compliance checks: " + request.getBatchId());
        
        try {
            VfdBatchResponse response = vfdBatchProcessingService.processBatchComplianceChecks(request);
            return successResponse("Batch compliance checks processed successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error processing batch compliance checks: " + e.getMessage());
            return errorResponse("Failed to process batch compliance checks", "500", null, null);
        }
    }

    /**
     * Get batch processing status
     */
    @GetMapping("/status/{batchId}")
    @Operation(summary = "Get Batch Status", description = "Get VFD batch processing status")
    public ResponseEntity<Map<String, Object>> getBatchProcessingStatus(@PathVariable String batchId) {
        logInfo("Fetching batch processing status: " + batchId);
        
        try {
            VfdBatchResponse response = vfdBatchProcessingService.getBatchProcessingStatus(batchId);
            return successResponse("Batch processing status retrieved successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error fetching batch processing status: " + e.getMessage());
            return errorResponse("Failed to fetch batch processing status", "500", null, null);
        }
    }

    /**
     * Get active batch jobs
     */
    @GetMapping("/active")
    @Operation(summary = "Get Active Batches", description = "Get active VFD batch processing jobs")
    public ResponseEntity<Map<String, Object>> getActiveBatchJobs() {
        logInfo("Fetching active VFD batch jobs");
        
        try {
            List<VfdBatchResponse> activeBatches = vfdBatchProcessingService.getActiveBatchJobs();
            return successResponse("Active batch jobs retrieved successfully", "200", null, activeBatches);
        } catch (Exception e) {
            logError("Error fetching active batch jobs: " + e.getMessage());
            return errorResponse("Failed to fetch active batch jobs", "500", null, null);
        }
    }

    /**
     * Cancel batch job
     */
    @PostMapping("/cancel/{batchId}")
    @Operation(summary = "Cancel Batch Job", description = "Cancel a VFD batch processing job")
    public ResponseEntity<Map<String, Object>> cancelBatchJob(@PathVariable String batchId) {
        logInfo("Cancelling batch job: " + batchId);
        
        try {
            VfdBatchResponse response = vfdBatchProcessingService.cancelBatchJob(batchId);
            return successResponse("Batch job cancelled successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error cancelling batch job: " + e.getMessage());
            return errorResponse("Failed to cancel batch job", "500", null, null);
        }
    }

    /**
     * Retry failed batch job
     */
    @PostMapping("/retry/{batchId}")
    @Operation(summary = "Retry Batch Job", description = "Retry a failed VFD batch processing job")
    public ResponseEntity<Map<String, Object>> retryBatchJob(@PathVariable String batchId) {
        logInfo("Retrying batch job: " + batchId);
        
        try {
            VfdBatchResponse response = vfdBatchProcessingService.retryBatchJob(batchId);
            return successResponse("Batch job retry initiated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error retrying batch job: " + e.getMessage());
            return errorResponse("Failed to retry batch job", "500", null, null);
        }
    }

    /**
     * Get batch processing history
     */
    @GetMapping("/history")
    @Operation(summary = "Get Batch History", description = "Get VFD batch processing history")
    public ResponseEntity<Map<String, Object>> getBatchProcessingHistory(
            @RequestParam(required = false) String batchType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        logInfo("Fetching VFD batch processing history");
        
        try {
            List<VfdBatchResponse> history = vfdBatchProcessingService.getBatchProcessingHistory(batchType, status, startDate, endDate);
            return successResponse("Batch processing history retrieved successfully", "200", null, history);
        } catch (Exception e) {
            logError("Error fetching batch processing history: " + e.getMessage());
            return errorResponse("Failed to fetch batch processing history", "500", null, null);
        }
    }

    /**
     * Get batch processing statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get Batch Statistics", description = "Get VFD batch processing statistics")
    public ResponseEntity<Map<String, Object>> getBatchProcessingStatistics(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String batchType) {
        logInfo("Fetching VFD batch processing statistics");
        
        try {
            Map<String, Object> statistics = vfdBatchProcessingService.getBatchProcessingStatistics(period, batchType);
            return successResponse("Batch processing statistics retrieved successfully", "200", null, statistics);
        } catch (Exception e) {
            logError("Error fetching batch processing statistics: " + e.getMessage());
            return errorResponse("Failed to fetch batch processing statistics", "500", null, null);
        }
    }

    /**
     * Schedule batch job
     */
    @PostMapping("/schedule")
    @Operation(summary = "Schedule Batch Job", description = "Schedule a VFD batch processing job")
    public ResponseEntity<Map<String, Object>> scheduleBatchJob(@RequestBody VfdBatchRequest request) {
        logInfo("Scheduling batch job: " + request.getBatchId());
        
        try {
            VfdBatchResponse response = vfdBatchProcessingService.scheduleBatchJob(request);
            return successResponse("Batch job scheduled successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error scheduling batch job: " + e.getMessage());
            return errorResponse("Failed to schedule batch job", "500", null, null);
        }
    }
}
