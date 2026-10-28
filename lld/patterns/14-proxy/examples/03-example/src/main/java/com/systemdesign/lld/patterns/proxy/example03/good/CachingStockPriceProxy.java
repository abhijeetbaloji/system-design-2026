package com.systemdesign.lld.patterns.proxy.example03.good;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Caching Proxy caching stock quotes with a configurable Time-To-Live (TTL).
 */
public class CachingStockPriceProxy implements StockPriceService {

    private static class CacheEntry {
        final StockQuote quote;
        final long expiresAt;

        CacheEntry(StockQuote quote, long expiresAt) {
            this.quote = quote;
            this.expiresAt = expiresAt;
        }

        boolean isExpired() {
            return System.currentTimeMillis() >= expiresAt;
        }
    }

    private final RealStockPriceService realService;
    private final long ttlMillis;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final AtomicInteger cacheHits = new AtomicInteger(0);
    private final AtomicInteger cacheMisses = new AtomicInteger(0);

    public CachingStockPriceProxy(RealStockPriceService realService, long ttlMillis) {
        this.realService = Objects.requireNonNull(realService, "RealStockPriceService cannot be null");
        if (ttlMillis <= 0) {
            throw new IllegalArgumentException("TTL must be greater than zero: " + ttlMillis);
        }
        this.ttlMillis = ttlMillis;
    }

    @Override
    public StockQuote getStockQuote(String ticker) {
        Objects.requireNonNull(ticker, "Ticker cannot be null");
        String symbol = ticker.toUpperCase();

        CacheEntry entry = cache.get(symbol);
        if (entry != null && !entry.isExpired()) {
            cacheHits.incrementAndGet();
            System.out.println("[CACHING PROXY] Cache HIT for '" + symbol + "' -> returning cached quote.");
            return entry.quote;
        }

        // Cache miss or expired entry
        cacheMisses.incrementAndGet();
        System.out.println("[CACHING PROXY] Cache MISS / EXPIRED for '" + symbol + "' -> delegating to RealSubject.");
        StockQuote freshQuote = realService.getStockQuote(symbol);
        cache.put(symbol, new CacheEntry(freshQuote, System.currentTimeMillis() + ttlMillis));
        return freshQuote;
    }

    @Override
    public boolean isMarketOpen() {
        return realService.isMarketOpen();
    }

    @Override
    public String getServiceName() {
        return "CachingProxy(" + realService.getServiceName() + ")";
    }

    public void evict(String ticker) {
        if (ticker != null) {
            cache.remove(ticker.toUpperCase());
            System.out.println("[CACHING PROXY] Evicted cache entry for: " + ticker.toUpperCase());
        }
    }

    public void clearCache() {
        cache.clear();
        System.out.println("[CACHING PROXY] Cleared all cache entries.");
    }

    public int getCacheHits() {
        return cacheHits.get();
    }

    public int getCacheMisses() {
        return cacheMisses.get();
    }

    public int getCacheSize() {
        return cache.size();
    }
}
