# 🏦 Banco Financiera API

## 📋 Description

Enterprise-grade REST API for financial institution customer management. This application provides comprehensive functionality for customer registration, financial product creation, and transactional operations with robust validation, error handling, and security features.

## ✨ Features

- 👥 **Customer Management**: Complete CRUD operations for customers
- 💳 **Financial Products**: Account creation and management (savings/current)
- 💰 **Transactions**: Deposits, withdrawals, and transfers between accounts
- 🛡️ **Security**: Basic authentication with BCrypt encryption
- 📚 **API Documentation**: Interactive Swagger UI
- ✅ **Validation**: Comprehensive input validation with Bean Validation
- 🚨 **Error Handling**: Structured exception handling by layers
- 📊 **Monitoring**: Health checks and metrics via Spring Actuator
- 🔄 **Transaction Management**: ACID compliance with @Transactional
- 📝 **Structured Logging**: Environment-specific logging configuration

## 🛠️ Requirements

- **Java 17**
- **Gradle 7.x+**
- **PostgreSQL 12+**
- **Docker & Docker Compose** (for database)

## 🚀 Quick Start

### 1. Database Setup
Use the included `docker-compose.yml` file:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    container_name: banco-financiera-postgres
    environment:
      POSTGRES_DB: banco-financiera
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init-scripts:/docker-entrypoint-initdb.d
    restart: unless-stopped
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U admin -d banco-financiera"]
      interval: 10s
      timeout: 5s
      retries: 5

  pgadmin:
    image: dpage/pgadmin4
    container_name: banco-financiera-pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@bancofin.com
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "8081:80"
    depends_on:
      - postgres
    restart: unless-stopped
    volumes:
      - pgadmin_data:/var/lib/pgadmin

volumes:
  postgres_data:
  pgadmin_data:
```

Start the database and pgAdmin:
```bash
docker compose up -d
```

### 2. Application Setup

```bash
# Clone the repository
git clone https://github.com/camounitropico/banco-financiera.git
cd banco-financiera

# Build the project
./gradlew build

# Run with dev profile (recommended for development)
./gradlew bootRun --args='--spring.profiles.active=dev'

# Or run with specific environment variables
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```

### 3. Verify Installation

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **API Docs**: http://localhost:8080/v3/api-docs
- **pgAdmin**: http://localhost:8081 (admin@bancofin.com / admin)

## 📚 Interactive API Documentation (Swagger/OpenAPI)

### Overview
The application automatically generates comprehensive API documentation using **SpringDoc OpenAPI 3**. No static files needed - everything is generated from your code annotations.

### How It Works
1. **Annotations in Controllers** → Automatic documentation
2. **DTO Validations** → Schema generation with constraints
3. **Live Testing** → Execute requests directly from the UI

### Accessing Documentation

**📋 Requirements**: Application must be running locally

#### 🌐 Web Interface (Recommended)
```bash
# Interactive Swagger UI
http://localhost:8080/swagger-ui.html

# Features:
# ✅ Try endpoints directly
# ✅ Built-in authentication
# ✅ Automatic request/response examples
# ✅ Schema validation in real-time
```

#### 📄 JSON Specification
```bash
# Raw OpenAPI 3.0 JSON
http://localhost:8080/v3/api-docs

# Use for:
# - Importing into Postman
# - Code generation tools
# - External documentation tools
```

### Authentication in Swagger UI
1. Click **"Authorize"** button (🔒)
2. Select **"basicAuth"**
3. Enter credentials:
   - **Username**: `user`
   - **Password**: `password`
4. Click **"Authorize"**
5. Now you can test all endpoints directly

### What's Automatically Documented
- ✅ **All endpoints** with descriptions
- ✅ **Request/Response schemas** from DTOs
- ✅ **Validation rules** (`@NotNull`, `@Email`, etc.)
- ✅ **Error responses** with status codes
- ✅ **Authentication requirements**
- ✅ **Business logic descriptions**

### Example: Testing User Creation
1. Go to `/swagger-ui.html`
2. Authorize with basic auth
3. Find **"Users"** → **POST** `/api/v1/banco-financiera/users`
4. Click **"Try it out"**
5. The form auto-populates with required fields
6. Execute and see real response

## 🔐 Authentication

The application uses Basic Authentication with the following default credentials:

| Username | Password | Role |
|----------|----------|------|
| `user` | `password` | USER |
| `admin` | `admin123` | ADMIN, USER |

### Environment Variables
```bash
SECURITY_USER_NAME=your_username
SECURITY_USER_PASSWORD=your_password
```

## 🌍 Profiles

### Development Profile (`dev`)
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```
- Detailed logging with correlation IDs
- Swagger UI enabled
- Full actuator endpoints
- PostgreSQL database

### Test Profile (`test`)
```bash
./gradlew test -Pspring.profiles.active=test
```
- H2 in-memory database
- Minimal logging
- Swagger disabled
- Optimized for testing

## 📡 API Endpoints

### User Management

#### Create User
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678923,
    "first_name": "andres",
    "last_name": "perez",
    "email": "somos@soytu.com",
    "birth_date": "1987-12-01"
  }'
```

#### Get All Users
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### Find User by Identification
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/users/12345678915' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### Update User
```bash
curl -X PUT 'localhost:8080/api/v1/banco-financiera/users/12345678910' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678910,
    "first_name": "juan",
    "last_name": "ibanez",
    "email": "soytu@soyyo.com",
    "birth_date": "2005-12-01"
  }'
```

#### Delete User
```bash
curl -X DELETE 'localhost:8080/api/v1/banco-financiera/users/12345678910' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

### Product Management

#### Create Product
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/products' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "account_type": "current",
    "account_balance": 50000,
    "exenta_gmf": false,
    "user_id": 2
  }'
```

#### Get All Products
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/products' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### Get Product by ID
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/products/{id}' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

### Transaction Management

#### Deposit
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/deposit' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 2000
  }'
```

#### Withdrawal
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/withdraw' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 2000
  }'
```

#### Transfer
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/transfer/2' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 2000
  }'
```

## 🏗️ Architecture & Improvements

### Tech Stack
- **Spring Boot 3.3.1** - Main framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Primary database
- **MapStruct** - DTO mapping
- **Bean Validation** - Input validation
- **Swagger/OpenAPI 3** - API documentation
- **Flyway** - Database migrations

### Key Improvements Implemented

#### 🛡️ Enhanced Security
- BCrypt password encoding
- Security headers (HSTS, Frame Options)
- Environment-configurable credentials
- Role-based access control

#### ✅ Comprehensive Validation
- Bean Validation annotations on DTOs
- Custom validation messages
- Business rule validations
- Input sanitization

#### 🚨 Advanced Error Handling
- Layered exception hierarchy (Business, Service, Data)
- Structured error responses with correlation IDs
- Specific exceptions for business scenarios:
  - `InsufficientFundsException`
  - `AccountNotFoundException`
  - `UserNotFoundException`
  - `AccountInactiveException`

#### 📊 Observability
- Structured logging with correlation IDs
- Environment-specific log levels
- Health checks and metrics
- Application monitoring endpoints

#### 🔄 Transaction Management
- ACID compliance with `@Transactional`
- Rollback strategies for failed operations
- Database connection pooling

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run integration tests
./gradlew integrationTest
```

## 📈 Monitoring

### Health Checks
- **GET** `/actuator/health` - Application health status
- **GET** `/actuator/info` - Application information

### Logs
Application logs are available in:
- **Development**: Console + `logs/banco-financiera-dev.log`
- **Production**: Console + `logs/banco-financiera.log`

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.