package tz.co.itrust.vfd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Base Service for VFD Microservice
 * Provides common functionality for all services
 */
public class BaseService {

    @Value("${notification_url:http://localhost:8084}")
    private String notificationUrl;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

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
            request.put("timestamp", java.time.LocalDateTime.now().toString());
            
            restTemplate.postForObject(notificationUrl + "/api/notifications/slack", request, Object.class);
        } catch (Exception e) {
            logger.error("Failed to send Slack notification: {}", e.getMessage());
        }
    }

    /**
     * Create Slack notification payload
     */
    protected static Map<String, String> createSlackPayload(String message, RestTemplate restTemplate, String notificationUrl) {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", message);
        payload.put("service", "itrust-vfd");
        payload.put("timestamp", java.time.LocalDateTime.now().toString());
        return payload;
    }
} 