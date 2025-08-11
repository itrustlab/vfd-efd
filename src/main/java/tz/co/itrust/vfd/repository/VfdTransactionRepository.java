package tz.co.itrust.vfd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz.co.itrust.vfd.entity.VfdTransaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * VFD Transaction Repository
 * Handles database operations for VFD transactions
 */
@Repository
public interface VfdTransactionRepository extends JpaRepository<VfdTransaction, Long> {

    /**
     * Find transaction by transaction ID
     */
    Optional<VfdTransaction> findByTransactionId(String transactionId);

    /**
     * Find transactions by customer ID
     */
    List<VfdTransaction> findByCustomerIdOrderByTransactionDateDesc(String customerId);

    /**
     * Find transactions by customer ID and status
     */
    List<VfdTransaction> findByCustomerIdAndStatusOrderByTransactionDateDesc(String customerId, VfdTransaction.TransactionStatus status);

    /**
     * Find transactions by VFD reference
     */
    Optional<VfdTransaction> findByVfdReference(String vfdReference);

    /**
     * Find transactions by external reference
     */
    Optional<VfdTransaction> findByExternalReference(String externalReference);

    /**
     * Find transactions by date range
     */
    @Query("SELECT t FROM VfdTransaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<VfdTransaction> findByTransactionDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Find transactions by customer ID and date range
     */
    @Query("SELECT t FROM VfdTransaction t WHERE t.customerId = :customerId AND t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<VfdTransaction> findByCustomerIdAndTransactionDateBetween(@Param("customerId") String customerId, 
                                                                   @Param("startDate") LocalDateTime startDate, 
                                                                   @Param("endDate") LocalDateTime endDate);

    /**
     * Find transactions by instrument code
     */
    List<VfdTransaction> findByInstrumentCodeOrderByTransactionDateDesc(String instrumentCode);

    /**
     * Count transactions by customer ID and status
     */
    long countByCustomerIdAndStatus(String customerId, VfdTransaction.TransactionStatus status);

    /**
     * Find pending transactions
     */
    List<VfdTransaction> findByStatusOrderByTransactionDateAsc(VfdTransaction.TransactionStatus status);

    /**
     * Count transactions by customer ID after a specific timestamp
     */
    @Query("SELECT COUNT(t) FROM VfdTransaction t WHERE t.customerId = :customerId AND t.transactionDate >= :timestamp")
    long countByCustomerIdAndTimestampAfter(@Param("customerId") String customerId, @Param("timestamp") LocalDateTime timestamp);
} 