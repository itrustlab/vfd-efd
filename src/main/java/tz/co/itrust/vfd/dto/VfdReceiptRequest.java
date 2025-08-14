package tz.co.itrust.vfd.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdReceiptRequest {

    @NotNull(message = "Invoice date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String idate;

    @NotNull(message = "Invoice time is required")
    @Pattern(regexp = "\\d{2}:\\d{2}(:\\d{2})?", message = "Time must be in HH:MM or HH:MM:SS format")
    private String itime;

    @NotBlank(message = "Customer invoice number is required")
    private String custinvoiceno;

    @NotNull(message = "Customer ID type is required")
    @Min(value = 1, message = "Customer ID type must be between 1 and 6")
    @Max(value = 6, message = "Customer ID type must be between 1 and 6")
    private Integer custidtype;

    private String custid;

    private String custname;

    private String mobilenum;

    private String branch;

    private String department;

    private String devicenumber;

    @NotNull(message = "Payment type is required")
    @Min(value = 1, message = "Payment type must be between 1 and 5")
    @Max(value = 5, message = "Payment type must be between 1 and 5")
    private Integer paytype;

    private String username;

    @NotNull(message = "Invoice details are required")
    @Size(min = 1, message = "At least one invoice detail is required")
    private List<VfdInvoiceDetail> invoiceDetails;

    // Optional fcode and fcodetoken - if not provided, will use configured values from properties
    private String fcode;
    private String fcodetoken;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VfdInvoiceDetail {
        @NotBlank(message = "Description is required")
        private String description;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer qty;

        @NotNull(message = "Tax code is required")
        @Min(value = 1, message = "Tax code must be between 1 and 5")
        @Max(value = 5, message = "Tax code must be between 1 and 5")
        private Integer taxcode;

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        private BigDecimal amt;
    }
}
