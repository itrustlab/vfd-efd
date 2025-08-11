package tz.co.itrust.vfd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.co.itrust.vfd.entity.VfdReconciliation;
import tz.co.itrust.vfd.entity.VfdReconciliation.ReconciliationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for VFD Reconciliation operations
 */
@Repository
public interface VfdReconciliationRepository extends JpaRepository<VfdReconciliation, Long> {

    /**
     * Find reconciliation by reconciliation ID
     */
    Optional<VfdReconciliation> findByReconciliationId(String reconciliationId);

    /**
     * Find reconciliations by status
     */
    List<VfdReconciliation> findByStatusOrderByStartTimeDesc(ReconciliationStatus status);

    /**
     * Find reconciliations within date range
     */
    @Query("SELECT r FROM VfdReconciliation r WHERE r.startTime BETWEEN :startDate AND :endDate " +
           "ORDER BY r.startTime DESC")
    List<VfdReconciliation> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Find reconciliations by status within date range
     */
    @Query("SELECT r FROM VfdReconciliation r WHERE r.status = :status " +
           "AND r.startTime BETWEEN :startDate AND :endDate " +
           "ORDER BY r.startTime DESC")
    List<VfdReconciliation> findByStatusAndDateRange(
            @Param("status") ReconciliationStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Find completed reconciliations
     */
    @Query("SELECT r FROM VfdReconciliation r WHERE r.status = 'COMPLETED' " +
           "AND r.endTime IS NOT NULL ORDER BY r.endTime DESC")
    List<VfdReconciliation> findCompletedReconciliations();

    /**
     * Find failed reconciliations
     */
    @Query("SELECT r FROM VfdReconciliation r WHERE r.status = 'FAILED' " +
           "ORDER BY r.startTime DESC")
    List<VfdReconciliation> findFailedReconciliations();

    /**
     * Count reconciliations by status
     */
    long countByStatus(ReconciliationStatus status);

    /**
     * Find reconciliations by created by user
     */
    List<VfdReconciliation> findByCreatedByOrderByCreatedAtDesc(String createdBy);

    /**
     * Find recent reconciliations
     */
    @Query("SELECT r FROM VfdReconciliation r WHERE r.startTime >= :since " +
           "ORDER BY r.startTime DESC")
    List<VfdReconciliation> findRecentReconciliations(@Param("since") LocalDateTime since);

    /**
     * Find reconciliations with high unmatched records
     */
    @Query("SELECT r FROM VfdReconciliation r WHERE r.unmatchedRecords > :threshold " +
           "ORDER BY r.unmatchedRecords DESC, r.startTime DESC")
    List<VfdReconciliation> findReconciliationsWithHighUnmatchedRecords(
            @Param("threshold") Integer threshold);
}
