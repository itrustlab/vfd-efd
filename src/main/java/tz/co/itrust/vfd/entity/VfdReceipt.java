package tz.co.itrust.vfd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vfd_receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "idate", nullable = false, length = 10)
    private String idate;

    @Column(name = "itime", nullable = false, length = 8)
    private String itime;

    @Column(name = "custinvoiceno", nullable = false, length = 255)
    private String custinvoiceno;

    @Column(name = "custidtype")
    private Integer custidtype;

    @Column(name = "custid", length = 255)
    private String custid;

    @Column(name = "custname", length = 255)
    private String custname;

    @Column(name = "mobilenum", length = 20)
    private String mobilenum;

    @Column(name = "branch", length = 255)
    private String branch;

    @Column(name = "department", length = 255)
    private String department;

    @Column(name = "devicenumber", length = 255)
    private String devicenumber;

    @Column(name = "paytype", nullable = false)
    private Integer paytype;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "vfd_response", columnDefinition = "TEXT")
    private String vfdResponse;

    @Column(name = "qr_code_path", length = 500)
    private String qrCodePath;

    @Column(name = "receipt_number", length = 255)
    private String receiptNumber;

    // New VFD response fields
    @Column(name = "rctvnum", length = 500)
    private String rctvnum;

    @Column(name = "rctvcode", length = 100)
    private String rctvcode;

    @Column(name = "znumber", length = 100)
    private String znumber;

    @Column(name = "qrpath", length = 500)
    private String qrpath;

    @Column(name = "qrcode_uri", columnDefinition = "LONGTEXT")
    private String qrcodeUri;

    @Column(name = "status_code_text", length = 100)
    private String statusCodeText;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // New VFD request fields
    @Column(name = "fcode_token", columnDefinition = "TEXT")
    private String fcodeToken;

    @Column(name = "fcode", length = 100)
    private String fcode;

    @Column(name = "custid_type")
    private Integer custidType;

    // Complete request/response storage
    @Column(name = "external_vfd_request", columnDefinition = "LONGTEXT")
    private String externalVfdRequest;

    @Column(name = "external_vfd_response", columnDefinition = "LONGTEXT")
    private String externalVfdResponse;

    // VFD status fields
    @Column(name = "vfd_status", length = 50)
    private String vfdStatus;

    @Column(name = "vfd_http_status")
    private Integer vfdHttpStatus;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<VfdReceiptDetail> receiptDetails;
}
