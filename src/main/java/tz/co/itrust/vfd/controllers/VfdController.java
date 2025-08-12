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
    public ResponseEntity<VfdReceiptResponse> processReceipt(
            @Valid @RequestBody VfdReceiptRequest request) {

        log.info("Received VFD receipt request: {}", request.getCustinvoiceno());
        VfdReceiptResponse response = vfdService.processReceipt(request);

        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
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