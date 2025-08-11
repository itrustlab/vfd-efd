package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for VFD reconciliation responses
 */
@Data
public class VfdReconciliationResponse {
    private String reconciliationId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalRecords;
    private int matchedRecords;
    private int unmatchedRecords;
    private List<Map<String, Object>> discrepancies;
    private String summary;
    private Map<String, Object> metadata;
}
