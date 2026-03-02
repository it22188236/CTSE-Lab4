# Microservices Lab - Option A Implementation

## Architecture Overview

This microservices lab consists of 4 services:

- **API Gateway** (Port 8080) - Central entry point for all requests
- **Item Service** (Port 8081) - Manages items/products
- **Order Service** (Port 8082) - Manages orders
- **Payment Service** (Port 8083) - Handles payment processing

## Services Configuration

### 1. API Gateway (Port 8080)

- Spring Cloud Gateway with REST controller
- Routes requests to backend services
- Provides unified access point

**Key Endpoints:**

- `GET /api/gateway/health` - Gateway health status
- `GET /api/gateway/services-status` - Status of all microservices
- `GET /api/gateway/all-items` - Proxy to item service
- `GET /api/gateway/all-orders` - Proxy to order service
- `GET /api/gateway/all-payments` - Proxy to payment service

### 2. Item Service (Port 8081)

- Manages product catalog
- CRUD operations on items

**Key Endpoints:**

- `GET /api/items` - Get all items
- `GET /api/items/{id}` - Get item by ID
- `POST /api/items` - Create new item
- `PUT /api/items/{id}` - Update item
- `DELETE /api/items/{id}` - Delete item
- `GET /api/items/health` - Service health status

### 3. Order Service (Port 8082)

- Manages customer orders
- CRUD operations on orders

**Key Endpoints:**

- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}/status` - Update order status
- `DELETE /api/orders/{id}` - Cancel order
- `GET /api/orders/health` - Service health status

### 4. Payment Service (Port 8083)

- Processes payments
- Manages payment records

**Key Endpoints:**

- `GET /api/payments` - Get all payments
- `GET /api/payments/{id}` - Get payment by ID
- `POST /api/payments` - Process payment
- `PUT /api/payments/{id}/status` - Update payment status
- `GET /api/payments/validate/{orderId}` - Validate payment for order
- `GET /api/payments/health` - Service health status

## Building the Services

### Option 1: Build from root directory (if using Maven wrapper)

```bash
# Navigate to api-gateway directory
cd api-gateway
mvn clean package

# Navigate to item-service directory
cd ../item-service/item-service
mvn clean package

# Navigate to order-service directory
cd ../../order-service/order-service
mvn clean package

# Navigate to payment-service directory
cd ../../payment-service/payment-service
mvn clean package
```

### Option 2: Build using Maven directly (if Maven is installed)

```bash
# In each service directory, run:
mvn clean install
```

## Running the Services

### Prerequisites

- Java 21 or higher
- Maven installed

### Start Services in Order

**Terminal 1 - API Gateway:**

```bash
cd api-gateway
mvn spring-boot:run
# Or
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

**Terminal 2 - Item Service:**

```bash
cd item-service/item-service
mvn spring-boot:run
# Or
java -jar target/item-service-0.0.1-SNAPSHOT.jar
```

**Terminal 3 - Order Service:**

```bash
cd order-service/order-service
mvn spring-boot:run
# Or
java -jar target/order-service-0.0.1-SNAPSHOT.jar
```

**Terminal 4 - Payment Service:**

```bash
cd payment-service/payment-service
mvn spring-boot:run
# Or
java -jar target/payment-service-0.0.1-SNAPSHOT.jar
```

## Testing the Services

### Using cURL

#### Check Gateway Health

```bash
curl http://localhost:8080/api/gateway/health
```

#### Check All Services Status

```bash
curl http://localhost:8080/api/gateway/services-status
```

#### Get All Items

```bash
curl http://localhost:8080/api/gateway/all-items
# Or directly
curl http://localhost:8081/api/items
```

#### Create New Item

```bash
curl -X POST http://localhost:8081/api/items \
  -H "Content-Type: application/json" \
  -d '{"name": "Headphones", "price": 149.99, "quantity": 20}'
```

#### Get All Orders

```bash
curl http://localhost:8080/api/gateway/all-orders
# Or directly
curl http://localhost:8082/api/orders
```

#### Create New Order

```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{"itemId": 1, "quantity": 3}'
```

#### Get All Payments

```bash
curl http://localhost:8080/api/gateway/all-payments
# Or directly
curl http://localhost:8083/api/payments
```

#### Process Payment

```bash
curl -X POST http://localhost:8083/api/payments \
  -H "Content-Type: application/json" \
  -d '{"orderId": 1, "amount": 1999.98}'
```

### Using Browser/Postman

Navigate to the following URLs or import into Postman:

- API Gateway Health: `http://localhost:8080/api/gateway/health`
- Services Status: `http://localhost:8080/api/gateway/services-status`
- All Items: `http://localhost:8081/api/items`
- All Orders: `http://localhost:8082/api/orders`
- All Payments: `http://localhost:8083/api/payments`

## Service Discovery (Optional)

The services are configured to work with Eureka service discovery. To enable full service discovery:

1. Set up a Eureka Server (Spring Cloud Eureka Server)
2. Update the Eureka configuration in `application.properties` files to point to your Eureka Server
3. Uncomment the Eureka client dependency in pom.xml if needed

## Notes

- All services use in-memory storage (ArrayList)
- Data will be reset when services restart
- For production, consider using a database for persistence
- Eureka Server setup is optional for basic lab purposes
