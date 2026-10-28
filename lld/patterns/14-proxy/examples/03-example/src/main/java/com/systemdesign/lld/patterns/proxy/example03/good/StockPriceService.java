package com.systemdesign.lld.patterns.proxy.example03.good;

public interface StockPriceService {
    StockQuote getStockQuote(String ticker);

    boolean isMarketOpen();

    String getServiceName();
}
