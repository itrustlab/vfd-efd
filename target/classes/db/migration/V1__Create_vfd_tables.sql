-- VFD Service Database Schema
-- Initial migration to create VFD receipts and receipt details tables

-- Create vfd_receipts table (with IF NOT EXISTS for existing databases)
CREATE TABLE IF NOT EXISTS vfd_receipts (
    id BIGSERIAL PRIMARY KEY,
    idate VARCHAR(10) NOT NULL,
    itime VARCHAR(8) NOT NULL,
    custinvoiceno VARCHAR(255) NOT NULL,
    custidtype INTEGER,
    custid VARCHAR(255),
    custname VARCHAR(255),
    mobilenum VARCHAR(20),
    branch VARCHAR(255),
    department VARCHAR(255),
    devicenumber VARCHAR(255),
    paytype INTEGER NOT NULL,
    username VARCHAR(255),
    total_amount NUMERIC(15,2),
    tax_amount NUMERIC(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    vfd_response TEXT,
    qr_code_path VARCHAR(500),
    receipt_number VARCHAR(255),
    rctvnum VARCHAR(255),
    rctvcode VARCHAR(255),
    znumber VARCHAR(255),
    vfdinvoicenum VARCHAR(255),
    senttime VARCHAR(255),
    message TEXT,
    status VARCHAR(50),
    qrpath VARCHAR(500),
    qrcode_uri VARCHAR(500),
    statuscode_text VARCHAR(255),
    statuscode INTEGER,
    error_message TEXT,
    vfd_status VARCHAR(50)
);

-- Create vfd_receipt_details table (with IF NOT EXISTS for existing databases)
CREATE TABLE IF NOT EXISTS vfd_receipt_details (
    id BIGSERIAL PRIMARY KEY,
    receipt_id BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL,
    qty INTEGER NOT NULL,
    taxcode INTEGER NOT NULL,
    amt NUMERIC(15,2) NOT NULL,
    CONSTRAINT fk_receipt_detail_receipt FOREIGN KEY (receipt_id) REFERENCES vfd_receipts(id) ON DELETE CASCADE
);

-- Create indexes for better performance (with IF NOT EXISTS for existing databases)
CREATE INDEX IF NOT EXISTS idx_vfd_receipts_custinvoiceno ON vfd_receipts(custinvoiceno);
CREATE INDEX IF NOT EXISTS idx_vfd_receipts_devicenumber ON vfd_receipts(devicenumber);
CREATE INDEX IF NOT EXISTS idx_vfd_receipts_created_at ON vfd_receipts(created_at);
CREATE INDEX IF NOT EXISTS idx_vfd_receipts_vfd_status ON vfd_receipts(vfd_status);
CREATE INDEX IF NOT EXISTS idx_vfd_receipts_rctvcode ON vfd_receipts(rctvcode);
CREATE INDEX IF NOT EXISTS idx_vfd_receipt_details_receipt_id ON vfd_receipt_details(receipt_id);

-- Add missing columns to existing table (if they don't exist)
DO $$ 
BEGIN
    -- Add VFD response columns if they don't exist
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'rctvnum') THEN
        ALTER TABLE vfd_receipts ADD COLUMN rctvnum VARCHAR(255);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'rctvcode') THEN
        ALTER TABLE vfd_receipts ADD COLUMN rctvcode VARCHAR(255);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'znumber') THEN
        ALTER TABLE vfd_receipts ADD COLUMN znumber VARCHAR(255);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'vfdinvoicenum') THEN
        ALTER TABLE vfd_receipts ADD COLUMN vfdinvoicenum VARCHAR(255);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'senttime') THEN
        ALTER TABLE vfd_receipts ADD COLUMN senttime VARCHAR(255);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'message') THEN
        ALTER TABLE vfd_receipts ADD COLUMN message TEXT;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'status') THEN
        ALTER TABLE vfd_receipts ADD COLUMN status VARCHAR(50);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'qrpath') THEN
        ALTER TABLE vfd_receipts ADD COLUMN qrpath VARCHAR(500);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'qrcode_uri') THEN
        ALTER TABLE vfd_receipts ADD COLUMN qrcode_uri VARCHAR(500);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'statuscode_text') THEN
        ALTER TABLE vfd_receipts ADD COLUMN statuscode_text VARCHAR(255);
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'statuscode') THEN
        ALTER TABLE vfd_receipts ADD COLUMN statuscode INTEGER;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'error_message') THEN
        ALTER TABLE vfd_receipts ADD COLUMN error_message TEXT;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'vfd_receipts' AND column_name = 'vfd_status') THEN
        ALTER TABLE vfd_receipts ADD COLUMN vfd_status VARCHAR(50);
    END IF;
END $$;

-- Add comments for documentation
COMMENT ON TABLE vfd_receipts IS 'Main table for storing VFD receipt information';
COMMENT ON TABLE vfd_receipt_details IS 'Table for storing VFD receipt line items/details';

COMMENT ON COLUMN vfd_receipts.custinvoiceno IS 'Customer invoice number - unique identifier';
COMMENT ON COLUMN vfd_receipts.vfd_status IS 'VFD processing status: success, error, pending';
COMMENT ON COLUMN vfd_receipts.rctvcode IS 'VFD receipt code from external system';
COMMENT ON COLUMN vfd_receipts.vfd_response IS 'Full VFD response from external system';
