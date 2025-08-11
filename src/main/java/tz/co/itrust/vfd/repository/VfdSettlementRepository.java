package tz.co.itrust.vfd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.co.itrust.vfd.entity.VfdSettlement;
import tz.co.itrust.vfd.entity.VfdSettlement.SettlementStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for VFD Settlement operations
 */
@Repository
public interface VfdSettlementRepository extends JpaRepository<VfdSettlement, Long> {

    /**
     * Find settlement by settlement ID
     */
    Optional<VfdSettlement> findBySettlementId(String settlementId);

    /**
     * Find settlement by transaction ID
     */
    Optional<VfdSettlement> findByTransactionId(String transactionId);

    /**
     * Find all pending settlements
     */
    List<VfdSettlement> findByStatusOrderByPriorityAscCreatedAtAsc(SettlementStatus status);

    /**
     * Find settlements by customer ID and status
     */
    List<VfdSettlement> findByCustomerIdAndStatusOrderByCreatedAtDesc(String customerId, SettlementStatus status);

    /**
     * Find settlements by customer ID within date range
     */
    @Query("SELECT s FROM VfdSettlement s WHERE s.customerId = :customerId " +
           "AND s.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY s.createdAt DESC")
    List<VfdSettlement> findByCustomerIdAndDateRange(
            @Param("customerId") String customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Find settlements by status within date range
     */
    @Query("SELECT s FROM VfdSettlement s WHERE s.status = :status " +
           "AND s.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY s.createdAt DESC")
    List<VfdSettlement> findByStatusAndDateRange(
            @Param("status") SettlementStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Count settlements by status
     */
    long countByStatus(SettlementStatus status);

    /**
     * Find settlements by broker code
     */
    List<VfdSettlement> findByBrokerCodeOrderByCreatedAtDesc(String brokerCode);

    /**
     * Find high priority settlements
     */
    @Query("SELECT s FROM VfdSettlement s WHERE s.priority >= :minPriority " +
           "AND s.status = :status ORDER BY s.priority DESC, s.createdAt ASC")
    List<VfdSettlement> findHighPrioritySettlements(
            @Param("minPriority") Integer minPriority,
            @Param("status") SettlementStatus status);
}
