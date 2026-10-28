package com.systemdesign.lld.patterns.proxy.example03.bad;

public class BadStockTraderDemo {
    public static void main(String[] args) {
        BadStockPriceService service = new BadStockPriceService();

        System.out.println("Trader 1 queries AAPL: $" + service.fetchPrice("AAPL"));
        System.out.println("Trader 2 queries AAPL: $" + service.fetchPrice("AAPL"));
        System.out.println("Trader 3 queries AAPL: $" + service.fetchPrice("AAPL"));

        System.out.println("Total expensive network calls made for identical data: " + service.getNetworkCalls());
    }
}
