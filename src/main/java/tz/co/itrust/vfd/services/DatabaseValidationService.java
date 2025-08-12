package tz.co.itrust.vfd.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import tz.co.itrust.vfd.repository.VfdReceiptRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.Set;

/**
 * Database Validation Service
 * Validates database schema on startup to ensure it matches JPA entities
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseValidationService {

    @PersistenceContext
    private EntityManager entityManager;

    private final VfdReceiptRepository receiptRepository;

    /**
     * Validate database schema on application startup
     */
    @EventListener(ApplicationReadyEvent.class)
    public void validateDatabaseSchema() {
        log.info("Starting database schema validation...");
        
        try {
            // Validate entity mappings
            validateEntityMappings();
            
            // Validate table structure
            validateTableStructure();
            
            // Test basic operations
            testBasicOperations();
            
            log.info("Database schema validation completed successfully!");
            
        } catch (Exception e) {
            log.error("Database schema validation failed: {}", e.getMessage(), e);
            throw new RuntimeException("Database schema validation failed", e);
        }
    }

    /**
     * Validate that all JPA entities can be mapped
     */
    private void validateEntityMappings() {
        log.info("Validating JPA entity mappings...");
        
        try {
            Metamodel metamodel = entityManager.getMetamodel();
            Set<EntityType<?>> entities = metamodel.getEntities();
            
            log.info("Found {} JPA entities", entities.size());
            
            for (EntityType<?> entity : entities) {
                String entityName = entity.getName();
                log.info("Validating entity: {}", entityName);
                
                // Try to create a simple query to validate the entity
                entityManager.createQuery("SELECT e FROM " + entityName + " e").setMaxResults(1);
                log.info("Entity {} is valid", entityName);
            }
            
        } catch (Exception e) {
            log.error("Entity mapping validation failed: {}", e.getMessage());
            throw new RuntimeException("Entity mapping validation failed", e);
        }
    }

    /**
     * Validate table structure by checking if required tables exist
     */
    private void validateTableStructure() {
        log.info("Validating table structure...");
        
        try {
            // Check if VFD tables exist by trying to access them
            long receiptCount = receiptRepository.count();
            log.info("VFD receipts table is accessible, current count: {}", receiptCount);
            
            // Try to access the entity manager to check table structure
            entityManager.createNativeQuery("SELECT COUNT(*) FROM vfd_receipts").getSingleResult();
            entityManager.createNativeQuery("SELECT COUNT(*) FROM vfd_receipt_details").getSingleResult();
            
            log.info("All required VFD tables are accessible");
            
        } catch (Exception e) {
            log.error("Table structure validation failed: {}", e.getMessage());
            throw new RuntimeException("Table structure validation failed. Please run the database-init.sql script first.", e);
        }
    }

    /**
     * Test basic database operations
     */
    private void testBasicOperations() {
        log.info("Testing basic database operations...");
        
        try {
            // Test repository operations
            long count = receiptRepository.count();
            log.info("Repository count operation successful: {}", count);
            
            // Test entity manager operations
            Object result = entityManager.createNativeQuery("SELECT 1").getSingleResult();
            log.info("Native query test successful: {}", result);
            
            log.info("Basic database operations test completed successfully");
            
        } catch (Exception e) {
            log.error("Basic database operations test failed: {}", e.getMessage());
            throw new RuntimeException("Basic database operations test failed", e);
        }
    }
}
