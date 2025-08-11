-- Migration: Create VFD Settlement Table
-- Version: 1
-- Description: Creates the vfd_settlements table for storing VFD settlement records

CREATE TABLE IF NOT EXISTS vfd_settlements (
    id BIGSERIAL PRIMARY KEY,
    settlement_id VARCHAR(50) UNIQUE NOT NULL,
    transaction_id VARCHAR(50) NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    instrument_code VARCHAR(20),
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'TZS',
    status VARCHAR(20) DEFAULT 'PENDING',
    settlement_time TIMESTAMP,
    priority INTEGER DEFAULT 1,
    message VARCHAR(500),
    broker_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_vfd_settlements_settlement_id ON vfd_settlements(settlement_id);
CREATE INDEX IF NOT EXISTS idx_vfd_settlements_transaction_id ON vfd_settlements(transaction_id);
CREATE INDEX IF NOT EXISTS idx_vfd_settlements_customer_id ON vfd_settlements(customer_id);
CREATE INDEX IF NOT EXISTS idx_vfd_settlements_status ON vfd_settlements(status);
CREATE INDEX IF NOT EXISTS idx_vfd_settlements_created_at ON vfd_settlements(created_at);
CREATE INDEX IF NOT EXISTS idx_vfd_settlements_broker_code ON vfd_settlements(broker_code);
CREATE INDEX IF NOT EXISTS idx_vfd_settlements_priority_status ON vfd_settlements(priority, status);

-- Add comments for documentation
COMMENT ON TABLE vfd_settlements IS 'VFD Settlement records for transaction settlements';
COMMENT ON COLUMN vfd_settlements.settlement_id IS 'Unique settlement identifier';
COMMENT ON COLUMN vfd_settlements.transaction_id IS 'Reference to the VFD transaction';
COMMENT ON COLUMN vfd_settlements.customer_id IS 'Customer identifier for the settlement';
COMMENT ON COLUMN vfd_settlements.instrument_code IS 'Financial instrument code';
COMMENT ON COLUMN vfd_settlements.amount IS 'Settlement amount';
COMMENT ON COLUMN vfd_settlements.currency IS 'Currency code (default: TZS)';
COMMENT ON COLUMN vfd_settlements.status IS 'Current settlement status';
COMMENT ON COLUMN vfd_settlements.settlement_time IS 'When the settlement was processed';
COMMENT ON COLUMN vfd_settlements.priority IS 'Processing priority (higher = more urgent)';
COMMENT ON COLUMN vfd_settlements.message IS 'Status message or description';
COMMENT ON COLUMN vfd_settlements.broker_code IS 'Broker identifier';
COMMENT ON COLUMN vfd_settlements.created_at IS 'Record creation timestamp';
COMMENT ON COLUMN vfd_settlements.updated_at IS 'Record last update timestamp';
COMMENT ON COLUMN vfd_settlements.created_by IS 'User who created the record';
COMMENT ON COLUMN vfd_settlements.updated_by IS 'User who last updated the record';
