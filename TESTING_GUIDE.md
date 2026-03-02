# Microservices Lab - Quick Testing Guide

## Testing with cURL or Postman

### 1. Gateway Health Check

```bash
curl http://localhost:8080/api/gateway/health
```

Expected Response:

```json
{
  "status": "API Gateway is running"
}
```

### 2. Check All Services Status

```bash
curl http://localhost:8080/api/gateway/services-status
```

Expected Response:

```json
{
  "item-service": "UP",
  "order-service": "UP",
  "payment-service": "UP"
}
```

---

## ITEM SERVICE TESTS

### 3. Get All Items

```bash
curl http://localhost:8081/api/items
```

Expected Response:

```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "quantity": 5
  },
  {
    "id": 2,
    "name": "Mouse",
    "price": 29.99,
    "quantity": 50
  },
  {
    "id": 3,
    "name": "Keyboard",
    "price": 79.99,
    "quantity": 30
  }
]
```

### 4. Get Item by ID

```bash
curl http://localhost:8081/api/items/1
```

Expected Response:

```json
{
  "id": 1,
  "name": "Laptop",
  "price": 999.99,
  "quantity": 5
}
```

### 5. Create New Item

```bash
curl -X POST http://localhost:8081/api/items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor",
    "price": 299.99,
    "quantity": 10
  }'
```

Expected Response:

```json
{
  "id": 4,
  "name": "Monitor",
  "price": 299.99,
  "quantity": 10
}
```

### 6. Update Item

```bash
curl -X PUT http://localhost:8081/api/items/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "price": 1499.99,
    "quantity": 3
  }'
```

### 7. Delete Item

```bash
curl -X DELETE http://localhost:8081/api/items/1
```

### 8. Item Service Health

```bash
curl http://localhost:8081/api/items/health
```

---

## ORDER SERVICE TESTS

### 9. Get All Orders

```bash
curl http://localhost:8082/api/orders
```

Expected Response:

```json
[
  {
    "id": 1,
    "itemId": 1,
    "quantity": 2,
    "status": "Pending"
  },
  {
    "id": 2,
    "itemId": 2,
    "quantity": 5,
    "status": "Completed"
  }
]
```

### 10. Get Order by ID

```bash
curl http://localhost:8082/api/orders/1
```

### 11. Create New Order

```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "itemId": 2,
    "quantity": 10
  }'
```

Expected Response:

```json
{
  "id": 3,
  "itemId": 2,
  "quantity": 10,
  "status": "Pending"
}
```

### 12. Update Order Status

```bash
curl -X PUT http://localhost:8082/api/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "Completed"
  }'
```

### 13. Cancel Order

```bash
curl -X DELETE http://localhost:8082/api/orders/1
```

### 14. Order Service Health

```bash
curl http://localhost:8082/api/orders/health
```

---

## PAYMENT SERVICE TESTS

### 15. Get All Payments

```bash
curl http://localhost:8083/api/payments
```

Expected Response:

```json
[
  {
    "id": 1,
    "orderId": 1,
    "amount": 1999.98,
    "status": "Completed"
  },
  {
    "id": 2,
    "orderId": 2,
    "amount": 149.95,
    "status": "Pending"
  }
]
```

### 16. Get Payment by ID

```bash
curl http://localhost:8083/api/payments/1
```

### 17. Process Payment

```bash
curl -X POST http://localhost:8083/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 1,
    "amount": 2999.98
  }'
```

Expected Response:

```json
{
  "id": 3,
  "orderId": 1,
  "amount": 2999.98,
  "status": "Processing"
}
```

### 18. Update Payment Status

```bash
curl -X PUT http://localhost:8083/api/payments/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "Completed"
  }'
```

### 19. Validate Payment

```bash
curl http://localhost:8083/api/payments/validate/1
```

Expected Response:

```json
{
  "orderId": 1,
  "isValid": true,
  "message": "Payment validation successful"
}
```

### 20. Payment Service Health

```bash
curl http://localhost:8083/api/payments/health
```

---

## GATEWAY TESTS (Proxy Endpoints)

### 21. Get All Items via Gateway

```bash
curl http://localhost:8080/api/gateway/all-items
```

### 22. Get All Orders via Gateway

```bash
curl http://localhost:8080/api/gateway/all-orders
```

### 23. Get All Payments via Gateway

```bash
curl http://localhost:8080/api/gateway/all-payments
```

---

## Testing Workflow Example

### Scenario: Complete Order-to-Payment Flow

#### Step 1: Check available items

```bash
curl http://localhost:8081/api/items
```

#### Step 2: Create an order for item 1 (2 units)

```bash
curl -X POST http://localhost:8082/api/orders \
  -H "Content-Type: application/json" \
  -d '{"itemId": 1, "quantity": 2}'
```

Note the returned order ID (e.g., 3)

#### Step 3: Process payment for the order

```bash
curl -X POST http://localhost:8083/api/payments \
  -H "Content-Type: application/json" \
  -d '{"orderId": 3, "amount": 1999.98}'
```

Note the returned payment ID

#### Step 4: Validate the payment

```bash
curl http://localhost:8083/api/payments/validate/3
```

#### Step 5: Update order status to Completed

```bash
curl -X PUT http://localhost:8082/api/orders/3/status \
  -H "Content-Type: application/json" \
  -d '{"status": "Completed"}'
```

#### Step 6: Verify everything via gateway

```bash
curl http://localhost:8080/api/gateway/services-status
```

---

## Testing with Postman

1. Create a new Postman collection called "Microservices Lab"
2. Add the following requests:

**Base URLs:**

- `{{gateway}}` = http://localhost:8080
- `{{items}}` = http://localhost:8081
- `{{orders}}` = http://localhost:8082
- `{{payments}}` = http://localhost:8083

3. Organize by folders: Gateway, Items, Orders, Payments
4. Use the URLs from this guide for each endpoint

## Troubleshooting

### Service not responding

- Ensure the service is started (check terminal window)
- Verify correct port number
- Check network connectivity

### Port already in use

- Ensure no other process is using the port
- Check if service is already running

### Gateway cannot reach backend services

- Verify all backend services are running
- Check firewall settings
- Ensure ports are not blocked
