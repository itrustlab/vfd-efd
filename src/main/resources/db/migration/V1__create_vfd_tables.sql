-- VFD Database Migration
-- Creates the initial VFD tables for the iTrust VFD microservice

-- Create VFD transactions table
CREATE TABLE IF NOT EXISTS vfd_transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(50) UNIQUE NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL, -- 'PURCHASE', 'SALE', 'TRANSFER', 'DIVIDEND'
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'TZS',
    instrument_code VARCHAR(20),
    instrument_name VARCHAR(100),
    quantity INTEGER,
    price_per_unit DECIMAL(10,2),
    transaction_date TIMESTAMP NOT NULL,
    settlement_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING', -- 'PENDING', 'COMPLETED', 'FAILED', 'CANCELLED'
    vfd_reference VARCHAR(100),
    external_reference VARCHAR(100),
    broker_code VARCHAR(20),
    broker_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Create VFD customer profiles table
CREATE TABLE IF NOT EXISTS vfd_customer_profiles (
    id BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(50) UNIQUE NOT NULL,
    vfd_account_number VARCHAR(50) UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    customer_type VARCHAR(20) NOT NULL, -- 'INDIVIDUAL', 'CORPORATE', 'INSTITUTIONAL'
    id_type VARCHAR(20), -- 'NIDA', 'PASSPORT', 'DRIVING_LICENSE', 'COMPANY_REG'
    id_number VARCHAR(50),
    phone_number VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    city VARCHAR(50),
    country VARCHAR(50) DEFAULT 'TANZANIA',
    tax_id VARCHAR(50),
    risk_profile VARCHAR(20) DEFAULT 'MODERATE', -- 'LOW', 'MODERATE', 'HIGH'
    account_status VARCHAR(20) DEFAULT 'ACTIVE', -- 'ACTIVE', 'SUSPENDED', 'CLOSED'
    kyc_status VARCHAR(20) DEFAULT 'PENDING', -- 'PENDING', 'APPROVED', 'REJECTED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

-- Create VFD instruments table
CREATE TABLE IF NOT EXISTS vfd_instruments (
    id BIGSERIAL PRIMARY KEY,
    instrument_code VARCHAR(20) UNIQUE NOT NULL,
    instrument_name VARCHAR(100) NOT NULL,
    instrument_type VARCHAR(20) NOT NULL, -- 'STOCK', 'BOND', 'ETF', 'MUTUAL_FUND'
    issuer_name VARCHAR(100),
    currency VARCHAR(3) DEFAULT 'TZS',
    face_value DECIMAL(10,2),
    issue_date DATE,
    maturity_date DATE,
    coupon_rate DECIMAL(5,2),
    market_cap DECIMAL(15,2),
    sector VARCHAR(50),
    status VARCHAR(20) DEFAULT 'ACTIVE', -- 'ACTIVE', 'SUSPENDED', 'DELISTED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create VFD holdings table
CREATE TABLE IF NOT EXISTS vfd_holdings (
    id BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    instrument_code VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    average_cost DECIMAL(10,2),
    market_value DECIMAL(15,2),
    unrealized_pnl DECIMAL(15,2),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(customer_id, instrument_code)
);

-- Create VFD API logs table
CREATE TABLE IF NOT EXISTS vfd_api_logs (
    id BIGSERIAL PRIMARY KEY,
    request_id VARCHAR(50) UNIQUE NOT NULL,
    endpoint VARCHAR(200) NOT NULL,
    method VARCHAR(10) NOT NULL,
    request_body TEXT,
    response_body TEXT,
    status_code INTEGER,
    response_time_ms INTEGER,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create VFD notifications table
CREATE TABLE IF NOT EXISTS vfd_notifications (
    id BIGSERIAL PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    notification_type VARCHAR(50) NOT NULL, -- 'TRANSACTION', 'SETTLEMENT', 'DIVIDEND', 'ANNOUNCEMENT'
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    priority VARCHAR(20) DEFAULT 'NORMAL', -- 'LOW', 'NORMAL', 'HIGH', 'URGENT'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_vfd_transactions_customer_id ON vfd_transactions(customer_id);
CREATE INDEX IF NOT EXISTS idx_vfd_transactions_transaction_date ON vfd_transactions(transaction_date);
CREATE INDEX IF NOT EXISTS idx_vfd_transactions_status ON vfd_transactions(status);
CREATE INDEX IF NOT EXISTS idx_vfd_transactions_vfd_reference ON vfd_transactions(vfd_reference);

CREATE INDEX IF NOT EXISTS idx_vfd_customer_profiles_vfd_account ON vfd_customer_profiles(vfd_account_number);
CREATE INDEX IF NOT EXISTS idx_vfd_customer_profiles_id_number ON vfd_customer_profiles(id_number);
CREATE INDEX IF NOT EXISTS idx_vfd_customer_profiles_status ON vfd_customer_profiles(account_status);

CREATE INDEX IF NOT EXISTS idx_vfd_holdings_customer_id ON vfd_holdings(customer_id);
CREATE INDEX IF NOT EXISTS idx_vfd_holdings_instrument_code ON vfd_holdings(instrument_code);

CREATE INDEX IF NOT EXISTS idx_vfd_api_logs_request_id ON vfd_api_logs(request_id);
CREATE INDEX IF NOT EXISTS idx_vfd_api_logs_created_at ON vfd_api_logs(created_at);

CREATE INDEX IF NOT EXISTS idx_vfd_notifications_customer_id ON vfd_notifications(customer_id);
CREATE INDEX IF NOT EXISTS idx_vfd_notifications_is_read ON vfd_notifications(is_read);
CREATE INDEX IF NOT EXISTS idx_vfd_notifications_created_at ON vfd_notifications(created_at);

-- Add comments for documentation
COMMENT ON TABLE vfd_transactions IS 'Stores all VFD transactions including purchases, sales, transfers, and dividends';
COMMENT ON TABLE vfd_customer_profiles IS 'Stores VFD customer profile information and KYC status';
COMMENT ON TABLE vfd_instruments IS 'Stores available VFD instruments (stocks, bonds, ETFs, etc.)';
COMMENT ON TABLE vfd_holdings IS 'Stores current holdings for each customer by instrument';
COMMENT ON TABLE vfd_api_logs IS 'Logs all API calls to external VFD systems for audit and debugging';
COMMENT ON TABLE vfd_notifications IS 'Stores notifications sent to customers about their VFD activities'; 