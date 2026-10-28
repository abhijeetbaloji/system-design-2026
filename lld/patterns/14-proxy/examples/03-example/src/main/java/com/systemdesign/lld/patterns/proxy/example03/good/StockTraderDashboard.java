package com.systemdesign.lld.patterns.proxy.example03.good;

import java.util.Objects;

/**
 * Client dashboard querying prices polymorphically through the StockPriceService interface.
 */
public class StockTraderDashboard {
    private final StockPriceService priceService;

    public StockTraderDashboard(StockPriceService priceService) {
        this.priceService = Objects.requireNonNull(priceService, "StockPriceService cannot be null");
    }

    public String displayTicker(String ticker) {
        StockQuote quote = priceService.getStockQuote(ticker);
        return String.format("=== TRADING DASHBOARD ===\nTicker:   %s\nPrice:    %s %.2f\nProvider: %s\n",
                quote.getTicker(), quote.getCurrency(), quote.getPrice(), priceService.getServiceName());
    }

    public StockPriceService getPriceService() {
        return priceService;
    }
}
