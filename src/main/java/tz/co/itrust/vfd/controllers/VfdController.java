package tz.co.itrust.vfd.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tz.co.itrust.vfd.dto.VfdReceiptRequest;
import tz.co.itrust.vfd.dto.VfdReceiptResponse;
import tz.co.itrust.vfd.services.VfdService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class VfdController {

    private final VfdService vfdService;

    @PostMapping("/receipt")
    public ResponseEntity<Map<String, Object>> processReceipt(
            @Valid @RequestBody VfdReceiptRequest request) {

        log.info("Received VFD receipt request: {}", request.getCustinvoiceno());
        
        try {
            VfdReceiptResponse response = vfdService.processReceipt(request);
            
            // Create GenericRestResponse format that API Gateway expects
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("rctvnum", response.getRctvnum());
            responseData.put("rctvcode", response.getRctvcode());
            responseData.put("znumber", response.getZnumber());
            responseData.put("vfdinvoicenum", response.getVfdinvoicenum());
            responseData.put("idate", response.getIdate());
            responseData.put("itime", response.getItime());
            responseData.put("senttime", response.getSenttime());
            responseData.put("message", response.getMessage());
            responseData.put("status", response.getStatus());
            responseData.put("qrpath", response.getQrpath());
            responseData.put("qrcode_uri", response.getQrcode_uri());
            responseData.put("statusCodeText", response.getStatusCodeText());
            responseData.put("statusCode", response.getStatusCode());
            responseData.put("errorMessage", response.getErrorMessage());
            
            // Create the exact GenericRestResponse format that API Gateway expects
            Map<String, Object> genericResponse = new HashMap<>();
            genericResponse.put("timestamp", LocalDateTime.now().toString());
            genericResponse.put("statusCode", "600"); // Use "600" like NBC for success
            genericResponse.put("message", "Success");
            genericResponse.put("data", responseData);
            
            // Return appropriate HTTP status based on the response
            if ("success".equals(response.getStatus())) {
                log.info("VFD service processed request successfully, returning response: {}", response.getStatus());
                log.info("=== VFD SUCCESS RESPONSE TO API GATEWAY ===");
                log.info("HTTP Status: 200 OK");
                log.info("Response Body: {}", genericResponse);
                log.info("Response Data: {}", responseData);
                log.info("==========================================");
                
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json")
                        .body(genericResponse);
            } else {
                // Return error response with appropriate HTTP status
                log.warn("VFD service returned error response: {}", response.getStatus());
                
                // For error responses, modify the generic response format
                genericResponse.put("statusCode", "612"); // Use "612" for service unavailable like NBC
                genericResponse.put("message", response.getMessage() != null ? response.getMessage() : "VFD Error");
                
                log.info("=== VFD ERROR RESPONSE TO API GATEWAY ===");
                log.info("VFD Status: {}", response.getStatus());
                log.info("VFD Message: {}", response.getMessage());
                log.info("VFD StatusCode: {}", response.getStatusCode());
                log.info("Response Body: {}", genericResponse);
                log.info("Response Data: {}", responseData);
                log.info("=========================================");
                
                if (response.getStatusCode() != null) {
                    return ResponseEntity.status(response.getStatusCode())
                            .header("Content-Type", "application/json")
                            .body(genericResponse);
                } else {
                    return ResponseEntity.badRequest()
                            .header("Content-Type", "application/json")
                            .body(genericResponse);
                }
            }
            
        } catch (Exception e) {
            log.error("Unexpected error in VFD controller: {}", e.getMessage(), e);
            
            // Return HTTP 500 for unexpected controller errors
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("status", "error");
            errorData.put("errorMessage", "Controller error: " + e.getMessage());
            errorData.put("statusCode", 500);
            errorData.put("statusCodeText", "Controller Error");
            
            Map<String, Object> genericErrorResponse = new HashMap<>();
            genericErrorResponse.put("timestamp", LocalDateTime.now().toString());
            genericErrorResponse.put("statusCode", "000"); // Use "000" for internal server error like NBC
            genericErrorResponse.put("message", "Controller error: " + e.getMessage());
            genericErrorResponse.put("data", errorData);
            
            log.info("=== VFD EXCEPTION RESPONSE TO API GATEWAY ===");
            log.info("HTTP Status: 500 Internal Server Error");
            log.info("Exception: {}", e.getMessage());
            log.info("Response Body: {}", genericErrorResponse);
            log.info("Error Data: {}", errorData);
            log.info("=============================================");
            
            return ResponseEntity.status(500).body(genericErrorResponse);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "VFD Service");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("message", "VFD Service is running");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "VFD Controller is working!");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "VFD Service");
        return ResponseEntity.ok(response);
    }
} 