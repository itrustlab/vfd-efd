package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for VFD report responses
 */
@Data
public class VfdReportResponse {
    private String reportId;
    private String reportType;
    private String status;
    private LocalDateTime generatedAt;
    private String downloadUrl;
    private Map<String, Object> summary;
    private String message;
    private Map<String, Object> metadata;
}
