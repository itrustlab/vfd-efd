package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

/**
 * DTO for VFD report requests
 */
@Data
public class VfdReportRequest {
    private String reportId;
    private String reportType;
    private LocalDate reportDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String format;
    private Map<String, Object> parameters;
    private String requestedBy;
    private String reportMonth;
    private String customerId;
    private String instrumentCode;
    private String brokerCode;
}
