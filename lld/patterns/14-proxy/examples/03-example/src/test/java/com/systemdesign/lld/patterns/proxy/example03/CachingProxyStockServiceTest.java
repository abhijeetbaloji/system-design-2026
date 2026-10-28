package com.systemdesign.lld.patterns.proxy.example03;

import com.systemdesign.lld.patterns.proxy.example03.good.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Caching Proxy Stock Price Service Tests")
class CachingProxyStockServiceTest {

    private RealStockPriceService realService;
    private CachingStockPriceProxy proxy;
    private StockTraderDashboard dashboard;

    @BeforeEach
    void setUp() {
        realService = new RealStockPriceService();
        proxy = new CachingStockPriceProxy(realService, 200); // 200ms TTL for unit tests
        dashboard = new StockTraderDashboard(proxy);
    }

    @Test
    @DisplayName("Initial request should be a cache miss and invoke RealStockPriceService")
    void testInitialCacheMiss() {
        assertEquals(0, realService.getNetworkCalls());
        assertEquals(0, proxy.getCacheHits());
        assertEquals(0, proxy.getCacheMisses());

        StockQuote quote = proxy.getStockQuote("AAPL");

        assertNotNull(quote);
        assertEquals("AAPL", quote.getTicker());
        assertEquals(185.25, quote.getPrice(), 0.001);
        assertEquals(1, realService.getNetworkCalls());
        assertEquals(1, proxy.getCacheMisses());
        assertEquals(0, proxy.getCacheHits());
        assertEquals(1, proxy.getCacheSize());
    }

    @Test
    @DisplayName("Immediate subsequent requests within TTL should hit cache and NOT invoke RealStockPriceService")
    void testCacheHits() {
        proxy.getStockQuote("NVDA"); // Miss #1, network call #1
        proxy.getStockQuote("NVDA"); // Hit #1
        proxy.getStockQuote("NVDA"); // Hit #2

        assertEquals(1, realService.getNetworkCalls());
        assertEquals(1, proxy.getCacheMisses());
        assertEquals(2, proxy.getCacheHits());
    }

    @Test
    @DisplayName("Requests after TTL expiration should trigger cache miss and fresh network call")
    void testTtlExpiration() throws InterruptedException {
        proxy.getStockQuote("MSFT"); // Miss #1, network call #1
        assertEquals(1, realService.getNetworkCalls());

        // Wait 250ms for 200ms TTL to expire
        Thread.sleep(250);

        proxy.getStockQuote("MSFT"); // Miss #2 (expired), network call #2
        assertEquals(2, realService.getNetworkCalls());
        assertEquals(2, proxy.getCacheMisses());
    }

    @Test
    @DisplayName("Manual cache eviction removes entry forcing fresh network call")
    void testManualEviction() {
        proxy.getStockQuote("GOOGL");
        assertEquals(1, realService.getNetworkCalls());

        proxy.evict("GOOGL");
        assertEquals(0, proxy.getCacheSize());

        proxy.getStockQuote("GOOGL");
        assertEquals(2, realService.getNetworkCalls());
    }

    @Test
    @DisplayName("Clear cache empties entire in-memory storage")
    void testClearCache() {
        proxy.getStockQuote("AAPL");
        proxy.getStockQuote("NVDA");
        assertEquals(2, proxy.getCacheSize());

        proxy.clearCache();
        assertEquals(0, proxy.getCacheSize());
    }

    @Test
    @DisplayName("StockTraderDashboard displays ticker info polymorphically")
    void testDashboardDisplay() {
        String output = dashboard.displayTicker("AAPL");
        assertNotNull(output);
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("185.25"));
        assertTrue(output.contains("CachingProxy"));
    }

    @Test
    @DisplayName("Invalid TTL throws IllegalArgumentException")
    void testInvalidTtl() {
        assertThrows(IllegalArgumentException.class, () -> new CachingStockPriceProxy(realService, -10));
    }
}
