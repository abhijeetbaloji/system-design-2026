package com.systemdesign.lld.patterns.facade.example02.good;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private final Map<String, Integer> stock = new HashMap<>();

    public InventoryService() {
        stock.put("SKU-LAPTOP", 5);
        stock.put("SKU-MOUSE", 20);
    }

    public synchronized boolean reserveStock(List<CartItem> items) {
        for (CartItem item : items) {
            int available = stock.getOrDefault(item.sku(), 0);
            if (available < item.quantity()) {
                System.out.println("[InventoryService] Insufficient stock for: " + item.sku() + " (requested: " + item.quantity() + ", available: " + available + ")");
                return false;
            }
        }
        for (CartItem item : items) {
            stock.put(item.sku(), stock.get(item.sku()) - item.quantity());
            System.out.println("[InventoryService] Stock reserved: " + item.quantity() + "x " + item.sku());
        }
        return true;
    }

    public synchronized void releaseStock(List<CartItem> items) {
        for (CartItem item : items) {
            stock.put(item.sku(), stock.getOrDefault(item.sku(), 0) + item.quantity());
            System.out.println("[InventoryService] Stock released: " + item.quantity() + "x " + item.sku());
        }
    }

    public int getAvailableStock(String sku) {
        return stock.getOrDefault(sku, 0);
    }
}
