package tz.co.itrust.vfd.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tz.co.itrust.vfd.dto.VfdReceiptRequest;
import tz.co.itrust.vfd.dto.VfdReceiptResponse;
import tz.co.itrust.vfd.entity.VfdReceipt;
import tz.co.itrust.vfd.entity.VfdReceiptDetail;
import tz.co.itrust.vfd.repository.VfdReceiptRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VfdService {

    private final RestTemplate restTemplate;
    private final VfdReceiptRepository receiptRepository;
    private final ObjectMapper objectMapper;

    @Value("${vfd.power-vfd-url:http://41.222.92.81:8082/power-vfd-new/apis/web/auth/receiver}")
    private String powerVfdUrl;

    @Value("${vfd.enabled:true}")
    private boolean vfdEnabled;

    @Value("${vfd.fcode:F1000}")
    private String fcode;

    @Value("${vfd.fcodetoken:YzJVME1qTnFWV2h6TURJekxUTTROR3B6WVVveU1ESXlMVEF5TFRBektrWXhNREF3S2pBNU9qVTJPakV6TURNMExYQmpkRE15T1MweU16Z3lNdz09}")
    private String fcodetoken;

    public VfdReceiptResponse processReceipt(VfdReceiptRequest request) {
        try {
            log.info("Processing VFD receipt request: {}", request.getCustinvoiceno());
            
            // Check if receipt already exists to prevent duplicate external VFD calls
            Optional<VfdReceipt> existingReceipt = receiptRepository.findByCustinvoicenoAndVfdStatusAndRctvcodeIsNotNull(
                request.getCustinvoiceno(), "success");
            if (existingReceipt.isPresent()) {
                VfdReceipt receipt = existingReceipt.get();
                log.info("Receipt {} already exists with successful VFD data (ID: {}), returning cached response", 
                    request.getCustinvoiceno(), receipt.getId());
                return buildResponseFromExistingReceipt(receipt);
            }
            
            // Check for any existing receipt (even failed ones) for logging purposes
            Optional<VfdReceipt> anyExistingReceipt = receiptRepository.findByCustinvoiceno(request.getCustinvoiceno());
            if (anyExistingReceipt.isPresent()) {
                VfdReceipt receipt = anyExistingReceipt.get();
                log.info("Receipt {} already exists (ID: {}) but with status: {}, will process fresh request", 
                    request.getCustinvoiceno(), receipt.getId(), receipt.getVfdStatus());
            }
            
            validateRequest(request);
            VfdReceiptResponse vfdResponse = forwardToPowerVfd(request);
            
            // Store receipt asynchronously after returning response
            try {
                storeReceipt(request, vfdResponse);
                } catch (Exception e) {
                log.error("Error storing receipt to database: {}", e.getMessage(), e);
                // Don't fail the request if storage fails
            }
            
            return vfdResponse;
        } catch (Exception e) {
            log.error("Error processing VFD receipt: {}", e.getMessage(), e);
            return VfdReceiptResponse.builder()
                    .status("error")
                    .errorMessage("Failed to process receipt: " + e.getMessage())
                    .statusCode(500)
                    .statusCodeText("Internal Server Error")
                    .build();
        }
    }

    private VfdReceiptResponse forwardToPowerVfd(VfdReceiptRequest request) {
        if (!vfdEnabled) {
            log.warn("VFD is disabled, returning error response");
            return VfdReceiptResponse.builder()
                    .status("error")
                    .errorMessage("VFD service is currently disabled")
                    .statusCode(503)
                    .statusCodeText("Service Unavailable")
                    .build();
        }
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Object transformedRequest = transformRequestToExternalFormat(request);

            HttpEntity<Object> entity = new HttpEntity<>(transformedRequest, headers);

            log.info("Forwarding request to Power-VFD: {}", powerVfdUrl);
            log.info("Transformed request format: {}", transformedRequest);

            ResponseEntity<VfdReceiptResponse> response = restTemplate.postForEntity(
                    powerVfdUrl, entity, VfdReceiptResponse.class);

            log.info("Power-VFD response: {}", response.getBody());
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Error forwarding to Power-VFD: {}", e.getMessage(), e);
            
            // For connection-related errors, return a proper error response
            if (e.getMessage() != null && (
                e.getMessage().contains("Connection timed out") || 
                e.getMessage().contains("timeout") ||
                e.getMessage().contains("ConnectException") ||
                e.getMessage().contains("Connection refused") ||
                e.getMessage().contains("I/O error") ||
                e.getMessage().contains("ResourceAccessException"))) {
                log.warn("External VFD system is unreachable, returning connection error response");
                return VfdReceiptResponse.builder()
                        .status("error")
                        .errorMessage("External VFD system is unreachable: " + e.getMessage())
                        .statusCode(503)
                        .statusCodeText("Service Unavailable")
                        .build();
            }
            
            // For other errors, return an error response
            log.error("Unexpected error from Power-VFD, returning error response");
            return VfdReceiptResponse.builder()
                    .status("error")
                    .errorMessage("Power-VFD system error: " + e.getMessage())
                    .statusCode(500)
                    .statusCodeText("Power-VFD Error")
                    .build();
        }
    }

    private Object transformRequestToExternalFormat(VfdReceiptRequest request) {
        // Transform to match the exact payload format specified by user
        Map<String, Object> requestBody = new HashMap<>();
        
        Map<String, Object> invoice = new HashMap<>();
        invoice.put("idate", request.getIdate());
        invoice.put("itime", request.getItime());
        invoice.put("custinvoiceno", request.getCustinvoiceno() != null ? request.getCustinvoiceno() : "");
        invoice.put("custidtype", String.valueOf(request.getCustidtype()));
        invoice.put("custid", request.getCustid() != null ? request.getCustid() : "");
        invoice.put("custname", request.getCustname() != null ? request.getCustname() : "");
        invoice.put("username", request.getUsername() != null ? request.getUsername() : "");
        invoice.put("branch", request.getBranch() != null ? request.getBranch() : "");
        invoice.put("department", request.getDepartment() != null ? request.getDepartment() : "");
        invoice.put("device_number", request.getDevicenumber() != null ? request.getDevicenumber() : "");
        invoice.put("paytype", String.valueOf(request.getPaytype()));
        // Include fcode and fcodetoken within each invoice object as specified
        invoice.put("fcode", fcode);
        invoice.put("fcodetoken", fcodetoken);

        List<Map<String, Object>> invoiceDetails = request.getInvoiceDetails().stream()
                .map(detail -> {
                    Map<String, Object> detailMap = new HashMap<>();
                    detailMap.put("description", detail.getDescription());
                    detailMap.put("qty", detail.getQty());
                    detailMap.put("taxcode", detail.getTaxcode());
                    detailMap.put("amt", detail.getAmt());
                    return detailMap;
                })
                .collect(Collectors.toList());

        invoice.put("invoiceDetails", invoiceDetails);

        List<Map<String, Object>> invoiceList = new ArrayList<>();
        invoiceList.add(invoice);
        requestBody.put("invoice", invoiceList);
        
        return requestBody;
    }

    private VfdReceipt storeReceipt(VfdReceiptRequest request, VfdReceiptResponse response) {
        try {
            BigDecimal totalAmount = request.getInvoiceDetails().stream()
                    .map(VfdReceiptRequest.VfdInvoiceDetail::getAmt)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Store the complete external request and response
            String externalRequest = null;
            String externalResponse = null;
            try {
                externalRequest = objectMapper.writeValueAsString(request);
                externalResponse = objectMapper.writeValueAsString(response);
            } catch (Exception e) {
                log.warn("Failed to serialize request/response: {}", e.getMessage());
            }

            VfdReceipt receipt = VfdReceipt.builder()
                    .idate(request.getIdate())
                    .itime(request.getItime())
                    .custinvoiceno(request.getCustinvoiceno())
                    .custidtype(request.getCustidtype())
                    .custid(request.getCustid())
                    .custname(request.getCustname())
                    .mobilenum(request.getMobilenum())
                    .branch(request.getBranch())
                    .department(request.getDepartment())
                    .devicenumber(request.getDevicenumber())
                    .paytype(request.getPaytype())
                    .username(request.getUsername())
                    .totalAmount(totalAmount)
                    .createdAt(LocalDateTime.now())
                    .vfdResponse(response != null ? response.toString() : null)
                    .qrCodePath(response != null ? response.getQrpath() : null)
                    .receiptNumber(response != null ? response.getVfdinvoicenum() : null)
                    // New VFD response fields
                    .rctvnum(response != null ? response.getRctvnum() : null)
                    .rctvcode(response != null ? response.getRctvcode() : null)
                    .znumber(response != null ? response.getZnumber() : null)
                    .qrpath(response != null ? response.getQrpath() : null)
                    .qrcodeUri(response != null ? response.getQrcode_uri() : null)
                    .statusCodeText(response != null ? response.getStatusCodeText() : null)
                    .errorMessage(response != null ? response.getErrorMessage() : null)
                    // New VFD request fields
                    .fcodeToken(fcodetoken)
                    .fcode(fcode)
                    .custidType(request.getCustidtype())
                    // Complete request/response storage
                    .externalVfdRequest(externalRequest)
                    .externalVfdResponse(externalResponse)
                    // VFD status fields
                    .vfdStatus(response != null ? response.getStatus() : null)
                    .vfdHttpStatus(response != null ? response.getStatusCode() : null)
                    .build();

            List<VfdReceiptDetail> details = request.getInvoiceDetails().stream()
                    .map(detail -> VfdReceiptDetail.builder()
                            .receipt(receipt)
                            .description(detail.getDescription())
                            .qty(detail.getQty())
                            .taxcode(detail.getTaxcode())
                            .amt(detail.getAmt())
                            .build())
                    .collect(Collectors.toList());

            receipt.setReceiptDetails(details);
            VfdReceipt savedReceipt = receiptRepository.save(receipt);
            log.info("Receipt saved with ID: {}", savedReceipt.getId());
            return savedReceipt;
            
        } catch (Exception e) {
            log.error("Error storing receipt: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to store receipt", e);
        }
    }

    private void validateRequest(VfdReceiptRequest request) {
        if (request.getCustidtype() >= 1 && request.getCustidtype() <= 5) {
            if (request.getCustid() == null || request.getCustid().trim().isEmpty()) {
                throw new IllegalArgumentException("Customer ID is required for ID type " + request.getCustidtype());
            }
        }

        if (request.getCustidtype() == 1 && request.getCustid() != null) {
            if (!request.getCustid().matches("\\d{9}")) {
                throw new IllegalArgumentException("TIN must be exactly 9 digits");
            }
        }
    }

    // Enhanced query methods using new fields
    public Optional<VfdReceipt> findByReceiptCode(String rctvcode) {
        return receiptRepository.findByRctvcode(rctvcode);
    }

    public Optional<VfdReceipt> findByZNumber(String znumber) {
        return receiptRepository.findByZnumber(znumber);
    }

    public List<VfdReceipt> findSuccessfulReceipts() {
        return receiptRepository.findByVfdStatusAndVfdHttpStatus("success", 200);
    }

    public List<VfdReceipt> findErrorReceipts() {
        return receiptRepository.findByVfdStatus("error");
    }

    public List<VfdReceipt> findReceiptsByDateRange(String startDate, String endDate) {
        return receiptRepository.findByIdateBetween(startDate, endDate);
    }

    public List<VfdReceipt> findReceiptsByStatus(String status) {
        return receiptRepository.findByVfdStatusOrderByCreatedAtDesc(status);
    }

    public Optional<VfdReceipt> findByReceiptCodeOrZNumber(String rctvcode, String znumber) {
        return receiptRepository.findByRctvcodeOrZnumber(rctvcode, znumber);
    }

    /**
     * Check if a receipt with given custinvoiceno already exists and has successful VFD data
     */
    public boolean isDuplicateReceipt(String custinvoiceno) {
        return receiptRepository.findByCustinvoicenoAndVfdStatusAndRctvcodeIsNotNull(custinvoiceno, "success").isPresent();
    }

    /**
     * Get cached receipt data for a given custinvoiceno
     */
    public Optional<VfdReceiptResponse> getCachedReceipt(String custinvoiceno) {
        Optional<VfdReceipt> receipt = receiptRepository.findByCustinvoicenoAndVfdStatusAndRctvcodeIsNotNull(custinvoiceno, "success");
        return receipt.map(this::buildResponseFromExistingReceipt);
    }

    /**
     * Build VfdReceiptResponse from existing receipt data (cached response)
     */
    private VfdReceiptResponse buildResponseFromExistingReceipt(VfdReceipt receipt) {
        log.info("Building cached response for receipt: {} (ID: {})", receipt.getCustinvoiceno(), receipt.getId());
        
        return VfdReceiptResponse.builder()
                .rctvnum(receipt.getRctvnum())
                .rctvcode(receipt.getRctvcode())
                .znumber(receipt.getZnumber())
                .vfdinvoicenum(receipt.getReceiptNumber() != null ? receipt.getReceiptNumber() : receipt.getCustinvoiceno())
                .idate(receipt.getIdate())
                .itime(receipt.getItime())
                .senttime(receipt.getCreatedAt() != null ? receipt.getCreatedAt().toString() : receipt.getItime())
                .message("Success (cached)")
                .status("success")
                .qrpath(receipt.getQrpath())
                .qrcode_uri(receipt.getQrcodeUri())
                .statusCodeText(receipt.getStatusCodeText() != null ? receipt.getStatusCodeText() : "HTTP_OK")
                .statusCode(receipt.getVfdHttpStatus() != null ? receipt.getVfdHttpStatus() : 200)
                .build();
    }

} 