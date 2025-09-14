# 🧪 Guía de Pruebas Locales - Banco Financiera API

## 📋 Índice

1. [Configuración Inicial](#configuración-inicial)
2. [Autenticación](#autenticación)
3. [Flujos de Prueba Completos](#flujos-de-prueba-completos)
4. [Testing por Endpoints](#testing-por-endpoints)
5. [Casos de Error y Validaciones](#casos-de-error-y-validaciones)
6. [Herramientas Recomendadas](#herramientas-recomendadas)
7. [Troubleshooting](#troubleshooting)

## 🚀 Configuración Inicial

### 1. Prerequisitos
```bash
# Verificar Java 17
java -version

# Verificar Docker
docker --version

# Clonar y posicionarse en el proyecto
git clone https://github.com/camounitropico/banco-financiera.git
cd banco-financiera
```

### 2. Levantar la Base de Datos
```bash
# El proyecto ya incluye docker-compose.yml con PostgreSQL y pgAdmin
# Levantar la base de datos y pgAdmin
docker compose up -d

# Verificar que estén corriendo
docker ps
```

### 3. Ejecutar la Aplicación
```bash
# Compilar y ejecutar en modo desarrollo
./gradlew bootRun --args='--spring.profiles.active=dev'

# O con variables de entorno
SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```

### 4. Verificar que esté funcionando
```bash
# Health check
curl http://localhost:8080/actuator/health

# Swagger UI (abrir en navegador)
http://localhost:8080/swagger-ui.html

# pgAdmin (abrir en navegador)
http://localhost:8081
# Usuario: admin@bancofin.com
# Contraseña: admin
```

## 🔐 Autenticación

### Credenciales por Defecto

| Usuario | Contraseña | Rol | Base64 (user:password) |
|---------|------------|-----|------------------------|
| `user` | `password` | USER | `dXNlcjpwYXNzd29yZA==` |
| `admin` | `admin123` | ADMIN | `YWRtaW46YWRtaW4xMjM=` |

### Ejemplos de Headers de Autenticación
```bash
# Con user:password
-H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='

# Con admin:admin123
-H 'Authorization: Basic YWRtaW46YWRtaW4xMjM='
```

## 🔄 Flujos de Prueba Completos

### Flujo 1: Gestión Completa de Usuario

#### Paso 1: Crear Usuario
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678901,
    "first_name": "Juan",
    "last_name": "Pérez",
    "email": "juan.perez@email.com",
    "birth_date": "1990-05-15"
  }'
```

**Respuesta Esperada (201):**
```json
{
  "id": 1,
  "identification_type": "CC",
  "identification_number": 12345678901,
  "first_name": "Juan",
  "last_name": "Pérez",
  "email": "juan.perez@email.com",
  "birth_date": "1990-05-15",
  "created_at": "2025-01-13T10:30:00",
  "updated_at": "2025-01-13T10:30:00"
}
```

#### Paso 2: Consultar Usuario Creado
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/users/12345678901' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### Paso 3: Actualizar Usuario
```bash
curl -X PUT 'localhost:8080/api/v1/banco-financiera/users/12345678901' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678901,
    "first_name": "Juan Carlos",
    "last_name": "Pérez García",
    "email": "juan.carlos@email.com",
    "birth_date": "1990-05-15"
  }'
```

### Flujo 2: Creación y Gestión de Productos Financieros

#### Paso 1: Crear Cuenta de Ahorros
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/products' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "account_type": "savings",
    "account_balance": 50000,
    "exenta_gmf": false,
    "user_id": 1
  }'
```

**Respuesta Esperada (200):**
```json
{
  "id": 1,
  "account_type": "savings",
  "account_number": "5312345678",
  "account_balance": 50000.0,
  "status": "active",
  "exenta_gmf": false,
  "user": {
    "id": 1,
    "first_name": "Juan Carlos",
    "last_name": "Pérez García"
  }
}
```

#### Paso 2: Crear Cuenta Corriente
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/products' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "account_type": "current",
    "account_balance": 100000,
    "exenta_gmf": true,
    "user_id": 1
  }'
```

#### Paso 3: Consultar Todos los Productos
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/products' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

### Flujo 3: Operaciones Transaccionales Completas

#### Paso 1: Depósito
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/deposit' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 25000
  }'
```

#### Paso 2: Retiro
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/withdraw' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 10000
  }'
```

#### Paso 3: Transferencia entre Cuentas
```bash
# Transferir de cuenta 1 a cuenta 2
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/transfer/2' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 15000
  }'
```

#### Paso 4: Verificar Saldos Actualizados
```bash
# Consultar cuenta origen
curl -X GET 'localhost:8080/api/v1/banco-financiera/products/1' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='

# Consultar cuenta destino
curl -X GET 'localhost:8080/api/v1/banco-financiera/products/2' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

## 📡 Testing por Endpoints

### Endpoints de Usuario (Recomendado)

#### GET - Obtener Todos los Usuarios
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### GET - Usuario por ID
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/users/12345678901' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### POST - Crear Usuario
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CE",
    "identification_number": 87654321098,
    "first_name": "María",
    "last_name": "González",
    "email": "maria.gonzalez@email.com",
    "birth_date": "1985-08-20"
  }'
```

#### PUT - Actualizar Usuario
```bash
curl -X PUT 'localhost:8080/api/v1/banco-financiera/users/87654321098' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CE",
    "identification_number": 87654321098,
    "first_name": "María Elena",
    "last_name": "González Rodríguez",
    "email": "maria.elena@email.com",
    "birth_date": "1985-08-20"
  }'
```

#### DELETE - Eliminar Usuario
```bash
curl -X DELETE 'localhost:8080/api/v1/banco-financiera/users/87654321098' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

### Endpoints de Productos

#### GET - Todos los Productos
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/products' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### GET - Producto por ID
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/products/1' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### POST - Crear Producto
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/products' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "account_type": "savings",
    "account_balance": 0,
    "exenta_gmf": false,
    "user_id": 1
  }'
```

#### PUT - Actualizar Estado de Producto
```bash
curl -X PUT "localhost:8080/api/v1/banco-financiera/products/1/status?status=INACTIVE" \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### DELETE - Eliminar Producto (solo si saldo = 0)
```bash
curl -X DELETE 'localhost:8080/api/v1/banco-financiera/products/1' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

### Endpoints de Transacciones

#### POST - Depósito
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/deposit' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 50000.50
  }'
```

#### POST - Retiro
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/withdraw' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 25000.25
  }'
```

#### POST - Transferencia
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/transfer/2' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 10000.00
  }'
```

## ❌ Casos de Error y Validaciones

### Errores de Validación (400 Bad Request)

#### Usuario Menor de Edad
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678902,
    "first_name": "Pedro",
    "last_name": "Joven",
    "email": "pedro@email.com",
    "birth_date": "2010-01-01"
  }'
```

**Respuesta Esperada:**
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Request validation failed",
  "status": 400,
  "path": "/api/v1/banco-financiera/users",
  "timestamp": "2025-01-13T10:30:00",
  "validation_errors": [
    {
      "field": "birthDate",
      "message": "Birth date must be in the past",
      "rejected_value": "2010-01-01"
    }
  ]
}
```

#### Email Inválido
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678903,
    "first_name": "Ana",
    "last_name": "Test",
    "email": "email-invalido",
    "birth_date": "1990-01-01"
  }'
```

#### Tipo de Cuenta Inválido
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/products' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "account_type": "invalid_type",
    "account_balance": 50000,
    "exenta_gmf": false,
    "user_id": 1
  }'
```

### Errores de Negocio

#### Fondos Insuficientes
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/withdraw' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 999999999
  }'
```

**Respuesta Esperada (400):**
```json
{
  "error": "INSUFFICIENT_FUNDS",
  "message": "Insufficient funds in account 1. Available: 50000.0, Requested: 999999999",
  "status": 400,
  "path": "/api/v1/transactions/withdraw/1",
  "timestamp": "2025-01-13T10:30:00"
}
```

#### Transferencia a la Misma Cuenta
```bash
curl -X POST 'localhost:8080/api/v1/banco-financiera/transactions/1/transfer/1' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "amount": 1000
  }'
```

#### Usuario No Encontrado (404)
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/users/99999999999' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

#### Usuario Duplicado (409)
```bash
# Crear usuario
curl -X POST 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678904,
    "first_name": "Test",
    "last_name": "Duplicate",
    "email": "duplicate@email.com",
    "birth_date": "1990-01-01"
  }'

# Intentar crear el mismo usuario
curl -X POST 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
  -H 'Content-Type: application/json' \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678904,
    "first_name": "Another",
    "last_name": "Name",
    "email": "different@email.com",
    "birth_date": "1990-01-01"
  }'
```

### Errores de Autenticación (401)

#### Sin Autenticación
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/users'
```

#### Credenciales Incorrectas
```bash
curl -X GET 'localhost:8080/api/v1/banco-financiera/users' \
  -H 'Authorization: Basic aW52YWxpZDppbnZhbGlk'
```

## 🛠️ Herramientas Recomendadas

### 1. **Swagger UI** (Recomendado)
- **URL**: http://localhost:8080/swagger-ui.html
- **Ventajas**:
  - Interfaz gráfica intuitiva
  - Autenticación integrada
  - Ejemplos automáticos
  - Testing directo desde el navegador

### 2. **Postman**
```bash
# Importar collection desde:
http://localhost:8080/v3/api-docs

# O crear requests manualmente con las URLs de esta guía
```

### 3. **curl** (Terminal)
- Perfecto para scripts automatizados
- Todos los ejemplos de esta guía usan curl

### 4. **HTTPie**
```bash
# Instalar
pip install httpie

# Ejemplo de uso
http POST localhost:8080/api/v1/banco-financiera/users \
  Authorization:"Basic dXNlcjpwYXNzd29yZA==" \
  identification_type=CC \
  identification_number:=12345678905 \
  first_name=Test \
  last_name=HTTPie \
  email=test@httpie.com \
  birth_date=1990-01-01
```

## 📊 Monitoreo y Logs

### Endpoints de Monitoreo

#### Health Check
```bash
curl http://localhost:8080/actuator/health
```

#### Application Info
```bash
curl http://localhost:8080/actuator/info \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA=='
```

### Logs en Tiempo Real
```bash
# Ver logs de la aplicación
tail -f logs/banco-financiera-dev.log

# Filtrar por correlation ID (cuando esté implementado)
grep "12345" logs/banco-financiera-dev.log
```

## 🔧 Troubleshooting

### Problemas Comunes

#### 1. **Error de Conexión a Base de Datos**
```bash
# Verificar que PostgreSQL esté corriendo
docker ps | grep postgres

# Reiniciar container si es necesario
docker-compose restart postgres

# Verificar logs de la base de datos
docker logs banco-financiera-db
```

#### 2. **Puerto 8080 Ya en Uso**
```bash
# Encontrar proceso usando el puerto
sudo lsof -i :8080

# Cambiar puerto en application.properties
echo "server.port=8081" >> src/main/resources/application-dev.properties
```

#### 3. **Errores de Compilación**
```bash
# Limpiar y recompilar
./gradlew clean build

# Verificar versión de Java
java -version
```

#### 4. **Swagger UI No Carga**
- Verificar que estés usando profile `dev`
- Verificar que el endpoint esté habilitado en SecurityConfig
- URL correcta: http://localhost:8080/swagger-ui.html

### Resetear Base de Datos
```bash
# Eliminar datos y containers
docker-compose down -v

# Levantar nuevamente
docker-compose up -d

# Esperar que Flyway ejecute las migraciones
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Comandos de Debug

#### Ver Todos los Endpoints Disponibles
```bash
curl http://localhost:8080/actuator/mappings \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' | jq
```

#### Verificar Configuración Activa
```bash
curl http://localhost:8080/actuator/env \
  -H 'Authorization: Basic dXNlcjpwYXNzd29yZA==' | jq '.activeProfiles'
```

---

## 🎯 Flujo de Prueba Completo Recomendado

### Script de Prueba Automatizada

```bash
#!/bin/bash

echo "🚀 Iniciando pruebas de Banco Financiera API..."

BASE_URL="http://localhost:8080"
AUTH_HEADER="Authorization: Basic dXNlcjpwYXNzd29yZA=="

# 1. Health Check
echo "1. ✅ Health Check"
curl -s $BASE_URL/actuator/health | jq

# 2. Crear Usuario
echo "2. 👤 Creando usuario..."
USER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/banco-financiera/users" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d '{
    "identification_type": "CC",
    "identification_number": 12345678999,
    "first_name": "Test",
    "last_name": "Automation",
    "email": "test.automation@email.com",
    "birth_date": "1990-01-01"
  }')

echo $USER_RESPONSE | jq

# 3. Crear Producto
echo "3. 💳 Creando cuenta de ahorros..."
PRODUCT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/banco-financiera/products" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d '{
    "account_type": "savings",
    "account_balance": 100000,
    "exenta_gmf": false,
    "user_id": 1
  }')

echo $PRODUCT_RESPONSE | jq

# 4. Hacer Depósito
echo "4. 💰 Realizando depósito..."
DEPOSIT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/v1/banco-financiera/transactions/1/deposit" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000
  }')

echo $DEPOSIT_RESPONSE | jq

# 5. Consultar Saldo Final
echo "5. 📊 Consultando saldo final..."
curl -s -X GET "$BASE_URL/api/v1/banco-financiera/products/1" \
  -H "$AUTH_HEADER" | jq

echo "🎉 ¡Pruebas completadas exitosamente!"
```

Guarda este script como `test-api.sh` y ejecútalo con:
```bash
chmod +x test-api.sh
./test-api.sh
```

---

Esta guía cubre todos los aspectos necesarios para realizar pruebas completas de la API en un entorno local. ¡Ahora tienes todo lo necesario para probar cada funcionalidad de manera sistemática!