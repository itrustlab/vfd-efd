package tz.co.itrust.vfd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * VFD Receipt Detail Entity
 * Stores individual item details for receipts
 * Based on Power VFD v2.1 specification
 */
@Entity
@Table(name = "vfd_receipt_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdReceiptDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false, foreignKey = @ForeignKey(name = "fk_receipt_detail_receipt"))
    private VfdReceipt receipt;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "taxcode", nullable = false)
    private Integer taxcode;

    @Column(name = "amt", nullable = false, precision = 15, scale = 2)
    private BigDecimal amt;
}
