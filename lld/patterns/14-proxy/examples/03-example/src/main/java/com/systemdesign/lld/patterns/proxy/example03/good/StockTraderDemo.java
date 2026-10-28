package com.systemdesign.lld.patterns.proxy.example03.good;

public class StockTraderDemo {
    public static void main(String[] args) throws InterruptedException {
        RealStockPriceService realFeed = new RealStockPriceService();
        // Configure caching proxy with 1-second TTL for demo
        CachingStockPriceProxy cachingProxy = new CachingStockPriceProxy(realFeed, 1000);
        StockTraderDashboard dashboard = new StockTraderDashboard(cachingProxy);

        System.out.println("--- Query 1: First request for AAPL (Cache Miss) ---");
        System.out.println(dashboard.displayTicker("AAPL"));

        System.out.println("--- Query 2: Immediate second request for AAPL (Cache Hit) ---");
        System.out.println(dashboard.displayTicker("AAPL"));

        System.out.println("--- Query 3: Immediate third request for AAPL (Cache Hit) ---");
        System.out.println(dashboard.displayTicker("AAPL"));

        System.out.println("Metrics -> Remote calls: " + realFeed.getNetworkCalls()
                + " | Hits: " + cachingProxy.getCacheHits()
                + " | Misses: " + cachingProxy.getCacheMisses());

        // Wait 1.2 seconds for TTL to expire
        System.out.println("\n[Waiting 1.2s for TTL expiration...]");
        Thread.sleep(1200);

        System.out.println("--- Query 4: Request after TTL expiry (Cache Miss) ---");
        System.out.println(dashboard.displayTicker("AAPL"));

        System.out.println("Final Metrics -> Remote calls: " + realFeed.getNetworkCalls()
                + " | Hits: " + cachingProxy.getCacheHits()
                + " | Misses: " + cachingProxy.getCacheMisses());
    }
}
