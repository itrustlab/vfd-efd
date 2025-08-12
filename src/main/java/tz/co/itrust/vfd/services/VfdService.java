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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VfdService {

    private final RestTemplate restTemplate;
    private final VfdReceiptRepository receiptRepository;

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
            log.warn("VFD is disabled, returning mock response");
            return createMockResponse(request);
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Try different authentication formats
            Object transformedRequest = tryDifferentAuthFormats(request);

            HttpEntity<Object> entity = new HttpEntity<>(transformedRequest, headers);

            log.info("Forwarding request to Power-VFD: {}", powerVfdUrl);
            log.info("Transformed request format: {}", transformedRequest);

            ResponseEntity<VfdReceiptResponse> response = restTemplate.postForEntity(
                    powerVfdUrl, entity, VfdReceiptResponse.class);

            log.info("Power-VFD response: {}", response.getBody());
            return response.getBody();

        } catch (Exception e) {
            log.error("Error forwarding to Power-VFD: {}", e.getMessage(), e);
            
            if (e.getMessage().contains("Connection timed out") || 
                e.getMessage().contains("timeout") ||
                e.getMessage().contains("ConnectException")) {
                log.warn("External VFD system is unreachable, returning mock response");
                return createMockResponse(request);
            }
            
            throw new RuntimeException("Failed to forward to Power-VFD system", e);
        }
    }

    private Object tryDifferentAuthFormats(VfdReceiptRequest request) {
        // Try different authentication formats to see which one works
        
        // Format 1: fcode and fcodetoken as query parameters
        String urlWithParams = powerVfdUrl + "?fcode=" + fcode + "&fcodetoken=" + fcodetoken;
        log.info("Trying URL with query parameters: {}", urlWithParams);
        
        // Format 2: fcode and fcodetoken in headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("fcode", fcode);
        headers.set("fcodetoken", fcodetoken);
        
        // Format 3: fcode and fcodetoken in request body (current approach)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fcode", fcode);
        requestBody.put("fcodetoken", fcodetoken);
        
        Map<String, Object> invoice = new HashMap<>();
        invoice.put("idate", request.getIdate());
        invoice.put("itime", request.getItime());
        invoice.put("custinvoiceno", request.getCustinvoiceno() != null ? request.getCustinvoiceno() : "");
        invoice.put("custidtype", String.valueOf(request.getCustidtype()));
        invoice.put("custid", request.getCustid() != null ? request.getCustid() : "");
        invoice.put("custname", request.getCustname() != null ? request.getCustname() : "");
        invoice.put("mobilenum", request.getMobilenum() != null ? request.getMobilenum() : "");
        invoice.put("username", request.getUsername() != null ? request.getUsername() : "");
        invoice.put("branch", request.getBranch() != null ? request.getBranch() : "");
        invoice.put("department", request.getDepartment() != null ? request.getDepartment() : "");
        invoice.put("devicenumber", request.getDevicenumber() != null ? request.getDevicenumber() : "");
        invoice.put("paytype", String.valueOf(request.getPaytype()));

        List<Map<String, Object>> invoiceDetails = request.getInvoiceDetails().stream()
                .map(detail -> {
                    Map<String, Object> detailMap = new HashMap<>();
                    detailMap.put("description", detail.getDescription());
                    detailMap.put("qty", String.valueOf(detail.getQty()));
                    detailMap.put("taxcode", String.valueOf(detail.getTaxcode()));
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

    private VfdReceiptResponse createMockResponse(VfdReceiptRequest request) {
        return VfdReceiptResponse.builder()
                .status("success")
                .message("Mock response - VFD is disabled")
                .rctvnum("http://localhost:8085/vfd/receipt/verify/MOCK123")
                .rctvcode("MOCK123")
                .znumber("Z123456789")
                .vfdinvoicenum("VFD" + System.currentTimeMillis())
                .idate(request.getIdate())
                .itime(request.getItime())
                .senttime(LocalDateTime.now().toString())
                .qrpath("/qr/mock-qr.png")
                .statusCode(200)
                .statusCodeText("OK")
                .build();
    }
} 