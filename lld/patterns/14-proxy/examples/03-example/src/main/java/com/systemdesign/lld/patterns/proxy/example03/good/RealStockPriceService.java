package com.systemdesign.lld.patterns.proxy.example03.good;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RealSubject managing remote connection to live financial stock exchange feed.
 */
public class RealStockPriceService implements StockPriceService {
    private final Map<String, Double> marketPrices = new ConcurrentHashMap<>();
    private final AtomicInteger networkCalls = new AtomicInteger(0);

    public RealStockPriceService() {
        marketPrices.put("AAPL", 185.25);
        marketPrices.put("NVDA", 128.50);
        marketPrices.put("MSFT", 435.00);
        marketPrices.put("GOOGL", 175.80);
    }

    @Override
    public StockQuote getStockQuote(String ticker) {
        Objects.requireNonNull(ticker, "Ticker cannot be null");
        String symbol = ticker.toUpperCase();

        networkCalls.incrementAndGet();
        System.out.println("[REAL SUBJECT] Establishing TLS socket connection to Exchange feed for '" + symbol + "'");

        Double price = marketPrices.get(symbol);
        if (price == null) {
            throw new IllegalArgumentException("Unknown stock ticker: " + ticker);
        }

        return new StockQuote(symbol, price, System.currentTimeMillis(), "USD");
    }

    @Override
    public boolean isMarketOpen() {
        return true;
    }

    @Override
    public String getServiceName() {
        return "LiveExchangeFeed";
    }

    public void updatePrice(String ticker, double newPrice) {
        marketPrices.put(ticker.toUpperCase(), newPrice);
    }

    public int getNetworkCalls() {
        return networkCalls.get();
    }
}
