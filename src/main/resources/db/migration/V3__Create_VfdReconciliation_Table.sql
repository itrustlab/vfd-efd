-- Migration: Create VFD Reconciliation Table
-- Version: 2
-- Description: Creates the vfd_reconciliations table for storing VFD reconciliation records

CREATE TABLE IF NOT EXISTS vfd_reconciliations (
    id BIGSERIAL PRIMARY KEY,
    reconciliation_id VARCHAR(50) UNIQUE NOT NULL,
    status VARCHAR(20) DEFAULT 'IN_PROGRESS',
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    total_records INTEGER DEFAULT 0,
    matched_records INTEGER DEFAULT 0,
    unmatched_records INTEGER DEFAULT 0,
    summary VARCHAR(1000),
    metadata TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_vfd_reconciliations_reconciliation_id ON vfd_reconciliations(reconciliation_id);
CREATE INDEX IF NOT EXISTS idx_vfd_reconciliations_status ON vfd_reconciliations(status);
CREATE INDEX IF NOT EXISTS idx_vfd_reconciliations_start_time ON vfd_reconciliations(start_time);
CREATE INDEX IF NOT EXISTS idx_vfd_reconciliations_end_time ON vfd_reconciliations(end_time);
CREATE INDEX IF NOT EXISTS idx_vfd_reconciliations_created_at ON vfd_reconciliations(created_at);
CREATE INDEX IF NOT EXISTS idx_vfd_reconciliations_created_by ON vfd_reconciliations(created_by);
CREATE INDEX IF NOT EXISTS idx_vfd_reconciliations_status_start_time ON vfd_reconciliations(status, start_time);

-- Add comments for documentation
COMMENT ON TABLE vfd_reconciliations IS 'VFD Reconciliation records for transaction reconciliations';
COMMENT ON COLUMN vfd_reconciliations.reconciliation_id IS 'Unique reconciliation identifier';
COMMENT ON COLUMN vfd_reconciliations.status IS 'Current reconciliation status';
COMMENT ON COLUMN vfd_reconciliations.start_time IS 'When the reconciliation process started';
COMMENT ON COLUMN vfd_reconciliations.end_time IS 'When the reconciliation process completed';
COMMENT ON COLUMN vfd_reconciliations.total_records IS 'Total number of records processed';
COMMENT ON COLUMN vfd_reconciliations.matched_records IS 'Number of records that matched successfully';
COMMENT ON COLUMN vfd_reconciliations.unmatched_records IS 'Number of records that did not match';
COMMENT ON COLUMN vfd_reconciliations.summary IS 'Summary of reconciliation results';
COMMENT ON COLUMN vfd_reconciliations.metadata IS 'Additional metadata in JSON format';
COMMENT ON COLUMN vfd_reconciliations.created_at IS 'Record creation timestamp';
COMMENT ON COLUMN vfd_reconciliations.updated_at IS 'Record last update timestamp';
COMMENT ON COLUMN vfd_reconciliations.created_by IS 'User who created the record';
COMMENT ON COLUMN vfd_reconciliations.updated_by IS 'User who last updated the record';
