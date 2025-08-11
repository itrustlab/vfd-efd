package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.dto.VfdReportRequest;
import tz.co.itrust.vfd.dto.VfdReportResponse;
import tz.co.itrust.vfd.services.VfdReportingService;

import java.time.LocalDate;
import java.util.Map;

/**
 * VFD Reporting Controller
 * Handles VFD compliance reports and regulatory reporting
 */
@RestController
@RequestMapping("/api/vfd/reporting")
@Tag(name = "VFD Reporting", description = "VFD compliance reports and regulatory reporting")
@RequiredArgsConstructor
public class VfdReportingController extends BaseController {

    private final VfdReportingService vfdReportingService;

    /**
     * Generate VFD daily transaction report
     */
    @PostMapping("/daily-transactions")
    @Operation(summary = "Generate Daily Report", description = "Generate VFD daily transaction report")
    public ResponseEntity<Map<String, Object>> generateDailyReport(@RequestBody VfdReportRequest request) {
        logInfo("Generating VFD daily transaction report for date: " + request.getReportDate());
        
        try {
            VfdReportResponse response = vfdReportingService.generateDailyReport(request);
            return successResponse("Daily report generated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error generating daily report: " + e.getMessage());
            return errorResponse("Failed to generate daily report", "500", null, null);
        }
    }

    /**
     * Generate VFD monthly compliance report
     */
    @PostMapping("/monthly-compliance")
    @Operation(summary = "Generate Monthly Compliance", description = "Generate VFD monthly compliance report")
    public ResponseEntity<Map<String, Object>> generateMonthlyComplianceReport(@RequestBody VfdReportRequest request) {
        logInfo("Generating VFD monthly compliance report for month: " + request.getReportMonth());
        
        try {
            VfdReportResponse response = vfdReportingService.generateMonthlyComplianceReport(request);
            return successResponse("Monthly compliance report generated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error generating monthly compliance report: " + e.getMessage());
            return errorResponse("Failed to generate monthly compliance report", "500", null, null);
        }
    }

    /**
     * Generate VFD customer activity report
     */
    @PostMapping("/customer-activity")
    @Operation(summary = "Generate Customer Activity", description = "Generate VFD customer activity report")
    public ResponseEntity<Map<String, Object>> generateCustomerActivityReport(@RequestBody VfdReportRequest request) {
        logInfo("Generating VFD customer activity report for customer: " + request.getCustomerId());
        
        try {
            VfdReportResponse response = vfdReportingService.generateCustomerActivityReport(request);
            return successResponse("Customer activity report generated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error generating customer activity report: " + e.getMessage());
            return errorResponse("Failed to generate customer activity report", "500", null, null);
        }
    }

    /**
     * Generate VFD instrument performance report
     */
    @PostMapping("/instrument-performance")
    @Operation(summary = "Generate Instrument Performance", description = "Generate VFD instrument performance report")
    public ResponseEntity<Map<String, Object>> generateInstrumentPerformanceReport(@RequestBody VfdReportRequest request) {
        logInfo("Generating VFD instrument performance report for instrument: " + request.getInstrumentCode());
        
        try {
            VfdReportResponse response = vfdReportingService.generateInstrumentPerformanceReport(request);
            return successResponse("Instrument performance report generated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error generating instrument performance report: " + e.getMessage());
            return errorResponse("Failed to generate instrument performance report", "500", null, null);
        }
    }

    /**
     * Generate VFD broker performance report
     */
    @PostMapping("/broker-performance")
    @Operation(summary = "Generate Broker Performance", description = "Generate VFD broker performance report")
    public ResponseEntity<Map<String, Object>> generateBrokerPerformanceReport(@RequestBody VfdReportRequest request) {
        logInfo("Generating VFD broker performance report for broker: " + request.getBrokerCode());
        
        try {
            VfdReportResponse response = vfdReportingService.generateBrokerPerformanceReport(request);
            return successResponse("Broker performance report generated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error generating broker performance report: " + e.getMessage());
            return errorResponse("Failed to generate broker performance report", "500", null, null);
        }
    }

    /**
     * Get available report types
     */
    @GetMapping("/types")
    @Operation(summary = "Get Report Types", description = "Get available VFD report types")
    public ResponseEntity<Map<String, Object>> getAvailableReportTypes() {
        logInfo("Fetching available VFD report types");
        
        try {
            Map<String, Object> reportTypes = vfdReportingService.getAvailableReportTypes();
            return successResponse("Report types retrieved successfully", "200", null, reportTypes);
        } catch (Exception e) {
            logError("Error fetching report types: " + e.getMessage());
            return errorResponse("Failed to fetch report types", "500", null, null);
        }
    }

    /**
     * Download report file
     */
    @GetMapping("/download/{reportId}")
    @Operation(summary = "Download Report", description = "Download generated VFD report file")
    public ResponseEntity<Map<String, Object>> downloadReport(@PathVariable String reportId) {
        logInfo("Downloading VFD report: " + reportId);
        
        try {
            Map<String, Object> downloadInfo = vfdReportingService.getReportDownloadInfo(reportId);
            return successResponse("Report download info retrieved successfully", "200", null, downloadInfo);
        } catch (Exception e) {
            logError("Error downloading report: " + e.getMessage());
            return errorResponse("Failed to download report", "500", null, null);
        }
    }
}
