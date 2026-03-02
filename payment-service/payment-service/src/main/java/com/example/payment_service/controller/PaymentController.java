package com.example.payment_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static List<Map<String, Object>> payments = new ArrayList<>();

    static {
        payments.add(Map.of("id", 1, "orderId", 1, "amount", 1999.98, "status", "Completed"));
        payments.add(Map.of("id", 2, "orderId", 2, "amount", 149.95, "status", "Pending"));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPayments() {
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPaymentById(@PathVariable int id) {
        Optional<Map<String, Object>> payment = payments.stream()
                .filter(p -> (int) p.get("id") == id)
                .findFirst();
        return payment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody Map<String, Object> newPayment) {
        int newId = payments.size() + 1;
        newPayment.put("id", newId);
        newPayment.put("status", "Processing");
        payments.add(newPayment);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPayment);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updatePaymentStatus(@PathVariable int id, @RequestBody Map<String, String> statusUpdate) {
        Optional<Map<String, Object>> existingPayment = payments.stream()
                .filter(p -> (int) p.get("id") == id)
                .findFirst();

        if (existingPayment.isPresent()) {
            Map<String, Object> payment = existingPayment.get();
            payment.put("status", statusUpdate.get("status"));
            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/validate/{orderId}")
    public ResponseEntity<Map<String, Object>> validatePayment(@PathVariable int orderId) {
        return ResponseEntity.ok(Map.of(
                "orderId", orderId,
                "isValid", true,
                "message", "Payment validation successful"
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "Payment Service is running"));
    }
}
