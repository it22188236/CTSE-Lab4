package com.example.item_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private static List<Map<String, Object>> items = new ArrayList<>();

    static {
        items.add(Map.of("id", 1, "name", "Laptop", "price", 999.99, "quantity", 5));
        items.add(Map.of("id", 2, "name", "Mouse", "price", 29.99, "quantity", 50));
        items.add(Map.of("id", 3, "name", "Keyboard", "price", 79.99, "quantity", 30));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllItems() {
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getItemById(@PathVariable int id) {
        Optional<Map<String, Object>> item = items.stream()
                .filter(i -> (int) i.get("id") == id)
                .findFirst();
        return item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createItem(@RequestBody Map<String, Object> newItem) {
        int newId = items.size() + 1;
        newItem.put("id", newId);
        items.add(newItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateItem(@PathVariable int id, @RequestBody Map<String, Object> updatedItem) {
        Optional<Map<String, Object>> existingItem = items.stream()
                .filter(i -> (int) i.get("id") == id)
                .findFirst();

        if (existingItem.isPresent()) {
            Map<String, Object> item = existingItem.get();
            updatedItem.put("id", id);
            items.set(items.indexOf(item), updatedItem);
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable int id) {
        items.removeIf(item -> (int) item.get("id") == id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "Item Service is running"));
    }
}
