# VFD Database Migrations

This document describes the database migrations for the VFD (Virtual Financial Data) microservice.

## Overview

The VFD microservice uses database migrations to manage schema changes and sample data. These migrations are designed to work with PostgreSQL and use Flyway for migration management.

## Migration Files

### V1__Create_VfdSettlement_Table.sql
- **Purpose**: Creates the `vfd_settlements` table for storing VFD settlement records
- **Tables Created**: `vfd_settlements`
- **Key Features**:
  - Settlement tracking with unique IDs
  - Priority-based processing
  - Status management (PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED)
  - Audit trail (created_at, updated_at, created_by, updated_by)
  - Performance indexes for common queries

### V2__Create_VfdReconciliation_Table.sql
- **Purpose**: Creates the `vfd_reconciliations` table for storing VFD reconciliation records
- **Tables Created**: `vfd_reconciliations`
- **Key Features**:
  - Reconciliation process tracking
  - Record matching statistics
  - Status management (IN_PROGRESS, COMPLETED, FAILED, CANCELLED)
  - Metadata storage for flexible data
  - Performance indexes for reporting queries

### V3__Insert_Sample_Data.sql
- **Purpose**: Inserts sample data for testing and development
- **Data Inserted**:
  - 8 sample settlements with various statuses
  - 12 sample reconciliations with different outcomes
  - Realistic timestamps and amounts

## Database Schema

### VFD Settlements Table
```sql
vfd_settlements
├── id (BIGSERIAL PRIMARY KEY)
├── settlement_id (VARCHAR(50) UNIQUE)
├── transaction_id (VARCHAR(50))
├── customer_id (VARCHAR(50))
├── instrument_code (VARCHAR(20))
├── amount (DECIMAL(15,2))
├── currency (VARCHAR(3))
├── status (VARCHAR(20))
├── settlement_time (TIMESTAMP)
├── priority (INTEGER)
├── message (VARCHAR(500))
├── broker_code (VARCHAR(20))
├── created_at (TIMESTAMP)
├── updated_at (TIMESTAMP)
├── created_by (VARCHAR(50))
└── updated_by (VARCHAR(50))
```

### VFD Reconciliations Table
```sql
vfd_reconciliations
├── id (BIGSERIAL PRIMARY KEY)
├── reconciliation_id (VARCHAR(50) UNIQUE)
├── status (VARCHAR(20))
├── start_time (TIMESTAMP)
├── end_time (TIMESTAMP)
├── total_records (INTEGER)
├── matched_records (INTEGER)
├── unmatched_records (INTEGER)
├── summary (VARCHAR(1000))
├── metadata (TEXT)
├── created_at (TIMESTAMP)
├── updated_at (TIMESTAMP)
├── created_by (VARCHAR(50))
└── updated_by (VARCHAR(50))
```

## Running Migrations

### Prerequisites
- PostgreSQL database server
- Database user with CREATE, INSERT, UPDATE privileges
- Flyway migration tool (if using Flyway)

### Manual Execution
1. Connect to your PostgreSQL database
2. Execute each migration file in order:
   ```bash
   psql -d your_database -f V1__Create_VfdSettlement_Table.sql
   psql -d your_database -f V2__Create_VfdReconciliation_Table.sql
   psql -d your_database -f V3__Insert_Sample_Data.sql
   ```

### Using Flyway
If your project uses Flyway, migrations will run automatically on application startup:
```bash
# Set Flyway configuration in application.properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

## Sample Data

### Settlement Statuses
- **PENDING**: 5 settlements awaiting processing
- **COMPLETED**: 3 settlements successfully processed
- **FAILED**: 1 settlement that failed processing

### Reconciliation Statuses
- **COMPLETED**: 10 successful reconciliations
- **FAILED**: 2 failed reconciliations
- **IN_PROGRESS**: 1 currently running reconciliation

### Priority Levels
- **Priority 1**: Standard priority (most settlements)
- **Priority 2**: Medium priority (2 settlements)
- **Priority 3**: Low priority (1 settlement)

## Performance Considerations

### Indexes Created
- Primary key indexes on all tables
- Unique indexes on business keys
- Composite indexes for common query patterns
- Timestamp indexes for date range queries

### Query Optimization
- Use status-based queries for filtering
- Leverage priority + status composite index for settlement processing
- Use date range indexes for historical queries

## Maintenance

### Regular Tasks
- Monitor index performance
- Review and clean up old data based on retention policies
- Update statistics for query optimization

### Backup Strategy
- Include migration files in version control
- Test migrations on staging environment before production
- Document any manual schema changes

## Troubleshooting

### Common Issues
1. **Duplicate Key Errors**: Check for existing data before running V3 migration
2. **Permission Errors**: Ensure database user has necessary privileges
3. **Timestamp Issues**: Verify PostgreSQL timezone settings

### Rollback Strategy
- Migrations are designed to be forward-only
- For rollbacks, create new migration files to reverse changes
- Test rollback procedures in staging environment

## Future Migrations

When adding new features, follow the naming convention:
- `V{version}__{description}.sql`
- Increment version numbers sequentially
- Include comprehensive documentation
- Test with sample data when appropriate
