package tz.co.itrust.vfd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.BaseController;
import tz.co.itrust.vfd.services.VfdDashboardService;

import java.util.Map;

/**
 * VFD Dashboard Controller
 * Handles VFD monitoring, analytics, and dashboard functionality
 */
@RestController
@RequestMapping("/api/vfd/dashboard")
@Tag(name = "VFD Dashboard", description = "VFD monitoring, analytics, and dashboard")
@RequiredArgsConstructor
public class VfdDashboardController extends BaseController {

    private final VfdDashboardService vfdDashboardService;

    /**
     * Get main dashboard overview
     */
    @GetMapping("/overview")
    @Operation(summary = "Get Dashboard Overview", description = "Get VFD main dashboard overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview() {
        logInfo("Fetching VFD dashboard overview");
        
        try {
            Map<String, Object> overview = vfdDashboardService.getDashboardOverview();
            return successResponse("Dashboard overview retrieved successfully", "200", null, overview);
        } catch (Exception e) {
            logError("Error fetching dashboard overview: " + e.getMessage());
            return errorResponse("Failed to fetch dashboard overview", "500", null, null);
        }
    }

    /**
     * Get transaction dashboard
     */
    @GetMapping("/transactions")
    @Operation(summary = "Get Transaction Dashboard", description = "Get VFD transaction dashboard")
    public ResponseEntity<Map<String, Object>> getTransactionDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String customerId) {
        logInfo("Fetching VFD transaction dashboard");
        
        try {
            Map<String, Object> dashboard = vfdDashboardService.getTransactionDashboard(period, customerId);
            return successResponse("Transaction dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching transaction dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch transaction dashboard", "500", null, null);
        }
    }

    /**
     * Get settlement dashboard
     */
    @GetMapping("/settlements")
    @Operation(summary = "Get Settlement Dashboard", description = "Get VFD settlement dashboard")
    public ResponseEntity<Map<String, Object>> getSettlementDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String status) {
        logInfo("Fetching VFD settlement dashboard");
        
        try {
            Map<String, Object> dashboard = vfdDashboardService.getSettlementDashboard(period, status);
            return successResponse("Settlement dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching settlement dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch settlement dashboard", "500", null, null);
        }
    }

    /**
     * Get compliance dashboard
     */
    @GetMapping("/compliance")
    @Operation(summary = "Get Compliance Dashboard", description = "Get VFD compliance dashboard")
    public ResponseEntity<Map<String, Object>> getComplianceDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String complianceType) {
        logInfo("Fetching VFD compliance dashboard");
        
        try {
            Map<String, Object> dashboard = vfdDashboardService.getComplianceDashboard(period, complianceType);
            return successResponse("Compliance dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching compliance dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch compliance dashboard", "500", null, null);
        }
    }

    /**
     * Get performance dashboard
     */
    @GetMapping("/performance")
    @Operation(summary = "Get Performance Dashboard", description = "Get VFD performance dashboard")
    public ResponseEntity<Map<String, Object>> getPerformanceDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String metric) {
        logInfo("Fetching VFD performance dashboard");
        
        try {
            Map<String, Object> dashboard = vfdDashboardService.getPerformanceDashboard(period, metric);
            return successResponse("Performance dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching performance dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch performance dashboard", "500", null, null);
        }
    }

    /**
     * Get risk dashboard
     */
    @GetMapping("/risk")
    @Operation(summary = "Get Risk Dashboard", description = "Get VFD risk dashboard")
    public ResponseEntity<Map<String, Object>> getRiskDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String riskType) {
        logInfo("Fetching VFD risk dashboard");
        
        try {
            Map<String, Object> dashboard = vfdDashboardService.getRiskDashboard(period, riskType);
            return successResponse("Risk dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching risk dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch risk dashboard", "500", null, null);
        }
    }

    /**
     * Get operational dashboard
     */
    @GetMapping("/operational")
    @Operation(summary = "Get Operational Dashboard", description = "Get VFD operational dashboard")
    public ResponseEntity<Map<String, Object>> getOperationalDashboard() {
        logInfo("Fetching VFD operational dashboard");
        
        try {
            Map<String, Object> dashboard = vfdDashboardService.getOperationalDashboard();
            return successResponse("Operational dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching operational dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch operational dashboard", "500", null, null);
        }
    }

    /**
     * Get customer dashboard
     */
    @GetMapping("/customers")
    @Operation(summary = "Get Customer Dashboard", description = "Get VFD customer dashboard")
    public ResponseEntity<Map<String, Object>> getCustomerDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String customerSegment) {
        logInfo("Fetching VFD customer dashboard");
        
        try {
            Map<String, Object> dashboard = vfdDashboardService.getCustomerDashboard(period, customerSegment);
            return successResponse("Customer dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching customer dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch customer dashboard", "500", null, null);
        }
    }

    /**
     * Get market dashboard
     */
    @GetMapping("/market")
    @Operation(summary = "Get Market Dashboard", description = "Get VFD market dashboard")
    public ResponseEntity<Map<String, Object>> getMarketDashboard(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String instrumentType) {
        logInfo("Fetching VFD market dashboard");
        
        try {
            Map<String, Object> dashboard = vfdDashboardService.getMarketDashboard(period, instrumentType);
            return successResponse("Market dashboard retrieved successfully", "200", null, dashboard);
        } catch (Exception e) {
            logError("Error fetching market dashboard: " + e.getMessage());
            return errorResponse("Failed to fetch market dashboard", "500", null, null);
        }
    }

    /**
     * Get real-time metrics
     */
    @GetMapping("/real-time")
    @Operation(summary = "Get Real-time Metrics", description = "Get VFD real-time metrics")
    public ResponseEntity<Map<String, Object>> getRealTimeMetrics() {
        logInfo("Fetching VFD real-time metrics");
        
        try {
            Map<String, Object> metrics = vfdDashboardService.getRealTimeMetrics();
            return successResponse("Real-time metrics retrieved successfully", "200", null, metrics);
        } catch (Exception e) {
            logError("Error fetching real-time metrics: " + e.getMessage());
            return errorResponse("Failed to fetch real-time metrics", "500", null, null);
        }
    }

    /**
     * Get dashboard configuration
     */
    @GetMapping("/config")
    @Operation(summary = "Get Dashboard Config", description = "Get VFD dashboard configuration")
    public ResponseEntity<Map<String, Object>> getDashboardConfiguration() {
        logInfo("Fetching VFD dashboard configuration");
        
        try {
            Map<String, Object> config = vfdDashboardService.getDashboardConfiguration();
            return successResponse("Dashboard configuration retrieved successfully", "200", null, config);
        } catch (Exception e) {
            logError("Error fetching dashboard configuration: " + e.getMessage());
            return errorResponse("Failed to fetch dashboard configuration", "500", null, null);
        }
    }

    /**
     * Update dashboard configuration
     */
    @PutMapping("/config")
    @Operation(summary = "Update Dashboard Config", description = "Update VFD dashboard configuration")
    public ResponseEntity<Map<String, Object>> updateDashboardConfiguration(@RequestBody Map<String, Object> config) {
        logInfo("Updating VFD dashboard configuration");
        
        try {
            Map<String, Object> response = vfdDashboardService.updateDashboardConfiguration(config);
            return successResponse("Dashboard configuration updated successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error updating dashboard configuration: " + e.getMessage());
            return errorResponse("Failed to update dashboard configuration", "500", null, null);
        }
    }

    /**
     * Export dashboard data
     */
    @PostMapping("/export")
    @Operation(summary = "Export Dashboard Data", description = "Export VFD dashboard data")
    public ResponseEntity<Map<String, Object>> exportDashboardData(@RequestBody Map<String, Object> request) {
        logInfo("Exporting VFD dashboard data");
        
        try {
            Map<String, Object> response = vfdDashboardService.exportDashboardData(request);
            return successResponse("Dashboard data exported successfully", "200", null, response);
        } catch (Exception e) {
            logError("Error exporting dashboard data: " + e.getMessage());
            return errorResponse("Failed to export dashboard data", "500", null, null);
        }
    }
}
