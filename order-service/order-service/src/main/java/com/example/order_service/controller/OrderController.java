package com.example.order_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static List<Map<String, Object>> orders = new ArrayList<>();

    static {
        orders.add(Map.of("id", 1, "itemId", 1, "quantity", 2, "status", "Pending"));
        orders.add(Map.of("id", 2, "itemId", 2, "quantity", 5, "status", "Completed"));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllOrders() {
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable int id) {
        Optional<Map<String, Object>> order = orders.stream()
                .filter(o -> (int) o.get("id") == id)
                .findFirst();
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> newOrder) {
        int newId = orders.size() + 1;
        newOrder.put("id", newId);
        newOrder.put("status", "Pending");
        orders.add(newOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable int id, @RequestBody Map<String, String> statusUpdate) {
        Optional<Map<String, Object>> existingOrder = orders.stream()
                .filter(o -> (int) o.get("id") == id)
                .findFirst();

        if (existingOrder.isPresent()) {
            Map<String, Object> order = existingOrder.get();
            order.put("status", statusUpdate.get("status"));
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable int id) {
        orders.removeIf(order -> (int) order.get("id") == id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "Order Service is running"));
    }
}
