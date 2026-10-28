package com.systemdesign.lld.patterns.facade.example02.bad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private final Map<String, Integer> stock = new HashMap<>();

    public InventoryService() {
        stock.put("SKU-LAPTOP", 10);
        stock.put("SKU-MOUSE", 25);
    }

    public boolean reserveStock(List<CartItem> items) {
        for (CartItem item : items) {
            int available = stock.getOrDefault(item.sku(), 0);
            if (available < item.quantity()) {
                System.out.println("[InventoryService] Out of stock for: " + item.sku());
                return false;
            }
        }
        for (CartItem item : items) {
            stock.put(item.sku(), stock.get(item.sku()) - item.quantity());
            System.out.println("[InventoryService] Reserved " + item.quantity() + " units of " + item.sku());
        }
        return true;
    }

    public void releaseStock(List<CartItem> items) {
        for (CartItem item : items) {
            stock.put(item.sku(), stock.getOrDefault(item.sku(), 0) + item.quantity());
            System.out.println("[InventoryService] Released " + item.quantity() + " units of " + item.sku());
        }
    }

    public int getAvailableStock(String sku) {
        return stock.getOrDefault(sku, 0);
    }
}
