package com.example.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "API Gateway is running"));
    }

    @GetMapping("/services-status")
    public ResponseEntity<Map<String, Object>> servicesStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            Map<String, String> itemHealth = restTemplate.getForObject("http://localhost:8081/api/items/health", Map.class);
            status.put("item-service", itemHealth != null ? "UP" : "DOWN");
        } catch (Exception e) {
            status.put("item-service", "DOWN");
        }

        try {
            Map<String, String> orderHealth = restTemplate.getForObject("http://localhost:8082/api/orders/health", Map.class);
            status.put("order-service", orderHealth != null ? "UP" : "DOWN");
        } catch (Exception e) {
            status.put("order-service", "DOWN");
        }

        try {
            Map<String, String> paymentHealth = restTemplate.getForObject("http://localhost:8083/api/payments/health", Map.class);
            status.put("payment-service", paymentHealth != null ? "UP" : "DOWN");
        } catch (Exception e) {
            status.put("payment-service", "DOWN");
        }

        return ResponseEntity.ok(status);
    }

    @GetMapping("/all-items")
    public ResponseEntity<?> getAllItems() {
        try {
            return ResponseEntity.ok(restTemplate.getForObject("http://localhost:8081/api/items", List.class));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Could not fetch items: " + e.getMessage()));
        }
    }

    @GetMapping("/all-orders")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(restTemplate.getForObject("http://localhost:8082/api/orders", List.class));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Could not fetch orders: " + e.getMessage()));
        }
    }

    @GetMapping("/all-payments")
    public ResponseEntity<?> getAllPayments() {
        try {
            return ResponseEntity.ok(restTemplate.getForObject("http://localhost:8083/api/payments", List.class));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Could not fetch payments: " + e.getMessage()));
        }
    }
}
