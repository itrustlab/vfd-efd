package tz.co.itrust.vfd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Base Controller for VFD Microservice
 * Provides common functionality for all controllers
 */
public class BaseController {

    @Value("${notification_url:http://localhost:8084}")
    private String notificationUrl;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Create error response
     */
    protected ResponseEntity<Map<String, Object>> errorResponse(String message, String messageCode, 
                                                               Map<String, Object> messages, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("success", false);
        response.put("code", messageCode);
        response.put("message", message);
        response.put("messages", messages != null ? messages : new HashMap<>());
        response.put("data", data);
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Create success response
     */
    protected ResponseEntity<Map<String, Object>> successResponse(String message, String successCode, 
                                                                 Map<String, Object> messages, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("success", true);
        response.put("code", successCode);
        response.put("message", message);
        response.put("messages", messages != null ? messages : new HashMap<>());
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Create exception response
     */
    protected ResponseEntity<Map<String, Object>> exceptionResponse(Exception exception, String statusCode) {
        if (statusCode == null) {
            statusCode = "500";
        }
        logger.error("Exception occurred: {}", exception.getMessage(), exception);
        logSlack("An exception occurred in VFD service: " + exception.getMessage());
        
        Map<String, Object> messages = new HashMap<>();
        return errorResponse("Internal Server Error", statusCode, messages, null);
    }

    /**
     * Log info message
     */
    protected void logInfo(String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    /**
     * Log warning message
     */
    protected void logWarn(String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    /**
     * Log error message
     */
    protected void logError(String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    /**
     * Send Slack notification asynchronously
     */
    @Async
    protected void logSlack(String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, String> request = new HashMap<>();
            request.put("message", message);
            request.put("service", "itrust-vfd");
            request.put("timestamp", LocalDateTime.now().toString());
            
            restTemplate.postForObject(notificationUrl + "/api/notifications/slack", request, Object.class);
        } catch (Exception e) {
            logger.error("Failed to send Slack notification: {}", e.getMessage());
        }
    }
} 