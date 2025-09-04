# VFD Service

Virtual Financial Data (VFD) microservice for iTrust platform.

## Features

- VFD receipt processing and forwarding
- External Power-VFD system integration
- Database storage with PostgreSQL
- RESTful API endpoints
- Mock response fallback for testing

## Quick Start

### Prerequisites

- Java 21
- Maven 3.6+
- PostgreSQL 12+

### Database Setup

1. Create database `itrust_service`
2. Flyway will automatically run migrations on startup
3. Update database credentials in `application.properties`

**Note**: The service now uses Flyway for database migrations. The initial schema will be created automatically when the service starts for the first time.

### Run Service

```bash
mvn spring-boot:run
```

Service will start on `http://localhost:8085/vfd`

## API Endpoints

- `POST /receipt` - Process VFD receipt
- `GET /health` - Health check
- `GET /test` - Test endpoint

## Configuration

Key properties in `application.properties`:

```properties
vfd.power-vfd-url=http://41.222.92.81:8082/power-vfd-new/apis/web/auth/receiver
vfd.fcode=F1000
vfd.fcodetoken=YzJVME1qTnFWV2h6TURJekxUTTROR3B6WVVveU1ESXlMVEF5TFRBektrWXhNREF3S2pBNU9qVTJPakV6TURNMExYQmpkRE15T1MweU16Z3lNdz09
vfd.timeout=120000
```

## Testing

Use the provided Postman collection `VFD-Simple-Postman-Collection.json` to test the endpoints.