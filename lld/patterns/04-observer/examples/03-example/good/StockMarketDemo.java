package good;

/**
 * Client Demonstration.
 *
 * Demonstrates:
 * 1. Attaching LiveTicker, PriceAlertBot, and TradingAnalyticsLogger.
 * 2. Emitting high-frequency ticks and verifying both Push and Pull mechanisms.
 * 3. Safely closing a widget via AutoCloseable (unregistering) to prevent lapsed listener leaks.
 * 4. Verifying subsequent ticks do not notify the closed widget.
 */
public class StockMarketDemo {

    public static void main(String[] args) {
        System.out.println("=== Factory & Observer Patterns: Stock Market Real-Time Feed Demo ===\n");

        StockMarket nasdaq = new StockMarket("NASDAQ");

        // Observers
        PriceAlertBot appleAlertBot = new PriceAlertBot("Apple-Breakout-Bot", "AAPL", 170.0, 190.0);
        TradingAnalyticsLogger analyticsLogger = new TradingAnalyticsLogger("QUANT-ANALYTICS-ENGINE");

        nasdaq.register(appleAlertBot);
        nasdaq.register(analyticsLogger);

        // Try-with-resources: LiveTickerDisplay widget registers itself and unregisters on close()
        try (LiveTickerDisplay desktopWidget = new LiveTickerDisplay("Desktop-Widget-1", nasdaq)) {
            // Tick 1
            nasdaq.setStockPrice("AAPL", 175.00, 1_250_000);

            // Tick 2
            nasdaq.setStockPrice("AAPL", 182.50, 1_840_000);

            // Tick 3: Crosses upper breakout threshold ($190) -> triggers PriceAlertBot!
            nasdaq.setStockPrice("AAPL", 192.00, 3_100_000);

            System.out.println("\n--- User closes Desktop Ticker Window ---");
        } // desktopWidget.close() is called automatically here!

        // Tick 4: After widget is closed. Only Alert Bot and Analytics Logger are notified
        nasdaq.setStockPrice("AAPL", 195.00, 2_400_000);

        System.out.printf("%nExecution Complete: Market feed '%s' has %d active observers remaining.%n",
                nasdaq.getExchangeName(), nasdaq.getObserverCount());
    }
}
