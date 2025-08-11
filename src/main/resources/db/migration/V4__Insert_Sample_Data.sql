-- Migration: Insert Sample Data
-- Version: 3
-- Description: Inserts sample data for VFD settlements and reconciliations for testing

-- Insert sample VFD settlements (with conflict handling)
INSERT INTO vfd_settlements (
    settlement_id, transaction_id, customer_id, instrument_code, 
    amount, currency, status, settlement_time, priority, message, broker_code, created_by
) VALUES 
    ('SETTLE_001', 'TXN_001', 'CUST_001', 'INST_001', 100000.00, 'TZS', 'PENDING', NULL, 1, 'Settlement pending processing', 'BROKER_001', 'SYSTEM'),
    ('SETTLE_002', 'TXN_002', 'CUST_002', 'INST_002', 150000.00, 'TZS', 'PENDING', NULL, 2, 'High priority settlement', 'BROKER_002', 'SYSTEM'),
    ('SETTLE_003', 'TXN_003', 'CUST_003', 'INST_003', 75000.00, 'TZS', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '2 hours', 1, 'Settlement completed successfully', 'BROKER_001', 'SYSTEM'),
    ('SETTLE_004', 'TXN_004', 'CUST_004', 'INST_004', 200000.00, 'TZS', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '4 hours', 1, 'Settlement completed successfully', 'BROKER_003', 'SYSTEM'),
    ('SETTLE_005', 'TXN_005', 'CUST_005', 'INST_005', 125000.00, 'TZS', 'PENDING', NULL, 3, 'Standard settlement', 'BROKER_001', 'SYSTEM'),
    ('SETTLE_006', 'TXN_006', 'CUST_006', 'INST_006', 300000.00, 'TZS', 'FAILED', NULL, 1, 'Settlement failed due to insufficient funds', 'BROKER_002', 'SYSTEM'),
    ('SETTLE_007', 'TXN_007', 'CUST_007', 'INST_007', 80000.00, 'TZS', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '6 hours', 1, 'Settlement completed successfully', 'BROKER_001', 'SYSTEM'),
    ('SETTLE_008', 'TXN_008', 'CUST_008', 'INST_008', 175000.00, 'TZS', 'PENDING', NULL, 2, 'Medium priority settlement', 'BROKER_003', 'SYSTEM')
ON CONFLICT (settlement_id) DO NOTHING;

-- Insert sample VFD reconciliations (with conflict handling)
INSERT INTO vfd_reconciliations (
    reconciliation_id, status, start_time, end_time, 
    total_records, matched_records, unmatched_records, summary, created_by
) VALUES 
    ('RECON_001', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '30 minutes', 150, 142, 8, 'Daily reconciliation completed successfully', 'SYSTEM'),
    ('RECON_002', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '25 minutes', 180, 175, 5, 'Daily reconciliation completed successfully', 'SYSTEM'),
    ('RECON_003', 'FAILED', CURRENT_TIMESTAMP - INTERVAL '3 days', NULL, 120, 95, 25, 'Reconciliation failed due to system error', 'SYSTEM'),
    ('RECON_004', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days' + INTERVAL '45 minutes', 200, 198, 2, 'Daily reconciliation completed successfully', 'SYSTEM'),
    ('RECON_005', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days' + INTERVAL '20 minutes', 160, 158, 2, 'Daily reconciliation completed successfully', 'SYSTEM'),
    ('RECON_006', 'IN_PROGRESS', CURRENT_TIMESTAMP - INTERVAL '2 hours', NULL, 90, 45, 0, 'Current reconciliation in progress', 'SYSTEM'),
    ('RECON_007', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days' + INTERVAL '35 minutes', 175, 170, 5, 'Daily reconciliation completed successfully', 'SYSTEM'),
    ('RECON_008', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '7 days' + INTERVAL '40 minutes', 190, 185, 5, 'Daily reconciliation completed successfully', 'SYSTEM'),
    ('RECON_009', 'FAILED', CURRENT_TIMESTAMP - INTERVAL '8 days', NULL, 140, 100, 40, 'Reconciliation failed due to data inconsistency', 'SYSTEM'),
    ('RECON_010', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP - INTERVAL '9 days' + INTERVAL '50 minutes', 165, 162, 3, 'Daily reconciliation completed successfully', 'SYSTEM'),
    ('RECON_011', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '15 minutes', 155, 153, 2, 'Daily reconciliation completed successfully', 'SYSTEM'),
    ('RECON_012', 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '11 days', CURRENT_TIMESTAMP - INTERVAL '11 days' + INTERVAL '55 minutes', 170, 168, 2, 'Daily reconciliation completed successfully', 'SYSTEM')
ON CONFLICT (reconciliation_id) DO NOTHING;

-- Update the created_at and updated_at timestamps to be more realistic
UPDATE vfd_settlements SET 
    created_at = CURRENT_TIMESTAMP - INTERVAL '1 day',
    updated_at = CURRENT_TIMESTAMP - INTERVAL '1 day'
WHERE settlement_id IN ('SETTLE_001', 'SETTLE_002', 'SETTLE_005', 'SETTLE_006', 'SETTLE_008');

UPDATE vfd_settlements SET 
    created_at = CURRENT_TIMESTAMP - INTERVAL '2 days',
    updated_at = CURRENT_TIMESTAMP - INTERVAL '2 days'
WHERE settlement_id IN ('SETTLE_003', 'SETTLE_004', 'SETTLE_007');

UPDATE vfd_reconciliations SET 
    created_at = start_time,
    updated_at = COALESCE(end_time, start_time);
