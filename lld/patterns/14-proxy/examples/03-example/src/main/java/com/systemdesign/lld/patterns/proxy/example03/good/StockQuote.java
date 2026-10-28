package com.systemdesign.lld.patterns.proxy.example03.good;

import java.util.Objects;

public class StockQuote {
    private final String ticker;
    private final double price;
    private final long timestamp;
    private final String currency;

    public StockQuote(String ticker, double price, long timestamp, String currency) {
        this.ticker = Objects.requireNonNull(ticker, "Ticker cannot be null").toUpperCase();
        this.price = price;
        this.timestamp = timestamp;
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
    }

    public String getTicker() {
        return ticker;
    }

    public double getPrice() {
        return price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return String.format("[%s: %s %.2f @ %d]", ticker, currency, price, timestamp);
    }
}
