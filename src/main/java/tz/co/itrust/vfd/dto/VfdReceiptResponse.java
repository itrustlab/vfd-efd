package tz.co.itrust.vfd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * VFD Receipt/Invoice Response DTO
 * Based on Power VFD v2.1 specification
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdReceiptResponse {

    // Success response fields
    private String rctvnum;        // Receipt verification URL
    private String rctvcode;       // Receipt verification code
    private String znumber;        // Z-number
    private String vfdinvoicenum;  // VFD invoice number
    private String idate;          // Invoice date
    private String itime;          // Invoice time
    private String senttime;       // Sent time
    private String message;        // Response message
    private String status;         // success/error
    private String qrpath;         // QR code file path
    private String qrcode_uri;     // QR code base64 data URI
    private String statusCodeText; // HTTP status text
    private Integer statusCode;    // HTTP status code

    // Error response fields
    private String errorMessage;   // Error message for failed requests
}
