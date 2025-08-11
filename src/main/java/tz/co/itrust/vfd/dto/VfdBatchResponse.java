package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for VFD batch responses
 */
@Data
public class VfdBatchResponse {
    private String batchId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int totalRecords;
    private int processedRecords;
    private int failedRecords;
    private List<Map<String, Object>> results;
    private String summary;
    private Map<String, Object> metadata;
}
