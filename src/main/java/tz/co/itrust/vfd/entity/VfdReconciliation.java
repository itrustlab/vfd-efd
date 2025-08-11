package tz.co.itrust.vfd.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * VFD Reconciliation Entity
 * Represents a VFD reconciliation record
 */
@Entity
@Table(name = "vfd_reconciliations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VfdReconciliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reconciliation_id", unique = true, nullable = false, length = 50)
    private String reconciliationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ReconciliationStatus status = ReconciliationStatus.IN_PROGRESS;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "total_records")
    private Integer totalRecords = 0;

    @Column(name = "matched_records")
    private Integer matchedRecords = 0;

    @Column(name = "unmatched_records")
    private Integer unmatchedRecords = 0;

    @Column(name = "summary", length = 1000)
    private String summary;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata; // JSON string for flexible metadata storage

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ReconciliationStatus {
        IN_PROGRESS, COMPLETED, FAILED, CANCELLED
    }
}
