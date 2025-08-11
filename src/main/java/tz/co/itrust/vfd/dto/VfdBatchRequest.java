package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for VFD batch requests
 */
@Data
public class VfdBatchRequest {
    private String batchId;
    private String batchType;
    private LocalDateTime batchDate;
    private List<Map<String, Object>> transactions;
    private Map<String, Object> parameters;
    private String initiatedBy;
    private String description;
}
