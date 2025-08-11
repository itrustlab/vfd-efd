package tz.co.itrust.vfd.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for VFD validation responses
 */
@Data
public class VfdValidationResponse {
    private String requestId;
    private boolean isValid;
    private List<String> validationErrors;
    private List<String> validationWarnings;
    private LocalDateTime validationTime;
    private Map<String, Object> validationDetails;
    private String status;
    private String message;
}
