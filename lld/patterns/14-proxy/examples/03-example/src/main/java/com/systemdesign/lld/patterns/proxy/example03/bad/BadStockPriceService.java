package com.systemdesign.lld.patterns.proxy.example03.bad;

import java.util.HashMap;
import java.util.Map;

public class BadStockPriceService {
    private final Map<String, Double> prices = new HashMap<>();
    private int networkCalls = 0;

    public BadStockPriceService() {
        prices.put("AAPL", 182.50);
        prices.put("NVDA", 125.80);
        prices.put("MSFT", 430.20);
    }

    // Flawed: Every call incurs simulated 100ms network round-trip latency and increments counter
    public double fetchPrice(String ticker) {
        networkCalls++;
        System.out.println("[EXPENSIVE REMOTE API] Connecting to NASDAQ feed for '" + ticker + "' (Call #" + networkCalls + ")");
        return prices.getOrDefault(ticker.toUpperCase(), 0.0);
    }

    public int getNetworkCalls() {
        return networkCalls;
    }
}
