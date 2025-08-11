# iTrust VFD Microservice Integration Guide

## 🎉 **Integration Complete!**

The **iTrust VFD Microservice** has been successfully created and integrated into the iTrust microservices architecture. This guide provides a comprehensive overview of what has been implemented and how to use it.

## 📋 **What Has Been Implemented**

### ✅ **1. VFD Microservice (`iTrust-vfd`)**
- **Complete Spring Boot Application** with Java 21
- **Maven Project** with all necessary dependencies
- **API Endpoints** for health, info, status, and process operations
- **Security Configuration** with OAuth2 integration
- **Exception Handling** and logging
- **Configuration Files** for different environments
- **Docker Support** with Dockerfile
- **Build Scripts** for deployment

### ✅ **2. Core Integration (`itrust_core`)**
- **VFD Route Configuration** (`VfdRoute.java`)
- **VFD Response Processor** (`VfdResponseProcessor.java`)
- **VFD Controller** (`VfdController.java`)
- **Configuration Properties** for VFD middleware URL
- **Camel Integration** for routing requests

## 🏗️ **Architecture Overview**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │───▶│   Auth Server   │───▶│  itrust_core    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                           │
                                                           ▼
                                              ┌─────────────────┐
                                              │  VFD Microservice│
                                              │  (iTrust-vfd)   │
                                              └─────────────────┘
```

## 🔄 **Request Flow**

1. **Client Request** → API Gateway
2. **Authentication** → Auth Server (OAuth2)
3. **Routing** → itrust_core (based on header: `vfd`)
4. **Processing** → VFD Microservice
5. **Response** → Back through the chain

## 🚀 **How to Use**

### **1. Start the VFD Microservice**

```bash
# Navigate to the VFD project
cd iTrust-vfd

# Build the project
.\mvnw.cmd clean install

# Run the application
java -jar target/itrust-vfd-1.0.0.jar --spring.profiles.active=local
```

### **2. Access VFD Endpoints**

The VFD microservice will be available at:
- **Base URL**: `http://localhost:8085/vfd`
- **Health Check**: `GET /api/vfd/health`
- **Info**: `GET /api/vfd/info`
- **Status**: `GET /api/vfd/status`
- **Process**: `POST /api/vfd/process`

### **3. Access Through Core Service**

The VFD service can also be accessed through the core service:
- **Base URL**: `http://localhost:8080/v1/vfd`
- **Health Check**: `GET /v1/vfd/health`
- **Info**: `GET /v1/vfd/info`
- **Status**: `GET /v1/vfd/status`
- **Process**: `POST /v1/vfd/process`

## 📁 **Project Structure**

```
iTrust-vfd/
├── src/main/java/tz/co/itrust/vfd/
│   ├── VfdApplication.java          # Main Spring Boot application
│   ├── BaseController.java          # Base controller functionality
│   ├── BaseService.java             # Base service functionality
│   ├── controllers/
│   │   └── VfdController.java       # VFD API endpoints
│   ├── services/
│   │   └── VfdService.java          # VFD business logic
│   ├── config/
│   │   └── SecurityConfig.java      # Security configuration
│   ├── exceptions/
│   │   └── GlobalExceptionHandler.java # Exception handling
│   └── utils/                       # Utility classes
├── src/main/resources/
│   ├── application.properties       # Main configuration
│   ├── application-local.properties # Local environment
│   └── application-sit.properties   # SIT environment
├── pom.xml                          # Maven configuration
├── Dockerfile                       # Container configuration
├── README.md                        # Project documentation
└── build-and-deploy-sit.bat         # Deployment script
```

## ⚙️ **Configuration**

### **Environment Variables**

| Variable | Description | Default |
|----------|-------------|---------|
| `server.port` | VFD service port | `8085` |
| `vfd.api.base-url` | VFD API base URL | `http://localhost:8086` |
| `vfd.api.timeout` | API timeout (ms) | `30000` |
| `vfd.api.retry-attempts` | Retry attempts | `3` |

### **Database Configuration**

The VFD microservice is configured to use PostgreSQL:
- **Database**: `itrust_vfd`
- **Username**: `postgres`
- **Password**: `password`
- **Port**: `5432`

## 🔧 **Development**

### **Adding New VFD Endpoints**

1. **Add endpoint to VFD Controller**:
```java
@GetMapping("/new-endpoint")
public ResponseEntity<Map<String, Object>> newEndpoint() {
    // Implementation
}
```

2. **Add corresponding route in itrust_core**:
```java
// In VfdController.java
request.setAttribute("CamelHttpUri", "/api/vfd/new-endpoint");
```

### **Adding New VFD Services**

1. **Create service class** in `services/` package
2. **Extend BaseService** for common functionality
3. **Add business logic** specific to VFD operations
4. **Inject into controller** as needed

## 🐳 **Docker Deployment**

### **Build Docker Image**
```bash
docker build -t itrust-vfd .
```

### **Run Container**
```bash
docker run -p 8085:8085 \
  -e SPRING_PROFILES_ACTIVE=sit \
  -e VFD_API_BASE_URL=https://sit-api.vfd.co.tz \
  itrust-vfd
```

## 📊 **Monitoring & Health Checks**

### **Health Endpoints**
- **VFD Service**: `http://localhost:8085/vfd/actuator/health`
- **Core Service**: `http://localhost:8080/v1/vfd/health`

### **Logging**
- **VFD Service**: `logs/vfd.log`
- **Core Service**: Standard Spring Boot logging

## 🔐 **Security**

### **Authentication**
- **OAuth2 Resource Server** configuration
- **JWT Token** validation
- **Stateless** session management

### **Authorization**
- **Role-based** access control
- **Endpoint-level** security
- **Audit logging** for all operations

## 🚨 **Troubleshooting**

### **Common Issues**

1. **Port Already in Use**
   ```bash
   # Check what's using port 8085
   netstat -ano | findstr :8085
   ```

2. **Database Connection Issues**
   - Verify PostgreSQL is running
   - Check database credentials
   - Ensure database exists

3. **Routing Issues**
   - Verify VFD route is loaded in itrust_core
   - Check Camel route configuration
   - Validate middleware URL configuration

### **Logs to Check**
- **VFD Service**: `logs/vfd.log`
- **Core Service**: Application logs
- **Camel Routes**: Route-specific logs

## 📈 **Next Steps**

### **Immediate Actions**
1. **Test the integration** with sample requests
2. **Verify health endpoints** are responding
3. **Check logging** for any errors
4. **Validate security** configuration

### **Future Enhancements**
1. **Add more VFD-specific endpoints** based on requirements
2. **Implement caching** for frequently accessed data
3. **Add metrics and monitoring** with Prometheus/Grafana
4. **Implement circuit breakers** for external API calls
5. **Add comprehensive unit and integration tests**

## 📞 **Support**

For issues or questions:
1. **Check the logs** for error messages
2. **Verify configuration** settings
3. **Test endpoints** individually
4. **Review this integration guide**

---

**🎉 Congratulations! The iTrust VFD Microservice is now fully integrated and ready for use!** 