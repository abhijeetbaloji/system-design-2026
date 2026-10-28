package good;

/**
 * Automated test suite for StockMarket Observer implementation.
 * Verifies Push/Pull functionality, threshold filtering, lapsed listener prevention,
 * and exception isolation.
 */
public class StockMarketTest {

    public static void main(String[] args) {
        System.out.println("Running StockMarketTest...");

        testLiveTickerReceivesUpdates();
        testPriceAlertBotFiltersThresholds();
        testPullModelMovingAverageCalculation();
        testLapsedListenerPreventionWithAutoCloseable();
        testFaultIsolationWhenObserverThrows();

        System.out.println("ALL 5 STOCK MARKET TESTS PASSED SUCCESSFULLY!\n");
    }

    private static void testLiveTickerReceivesUpdates() {
        StockMarket market = new StockMarket("NYSE");
        LiveTickerDisplay ticker = new LiveTickerDisplay("T1", market);

        market.setStockPrice("MSFT", 400.0, 500_000);
        market.setStockPrice("MSFT", 405.0, 600_000);

        assertEquals(2, ticker.getTickCount(), "Ticker should have received 2 price updates");
        ticker.close();
        System.out.println("  ✓ testLiveTickerReceivesUpdates passed.");
    }

    private static void testPriceAlertBotFiltersThresholds() {
        StockMarket market = new StockMarket("NYSE");
        PriceAlertBot bot = new PriceAlertBot("TeslaBot", "TSLA", 150.0, 200.0);
        market.register(bot);

        // Price in normal band (175): No alert
        market.setStockPrice("TSLA", 175.0, 100_000);
        assertEquals(0, bot.getAlertsTriggered(), "No alert within normal boundary");

        // Price breaches upper threshold (210 >= 200): 1 Alert
        market.setStockPrice("TSLA", 210.0, 250_000);
        assertEquals(1, bot.getAlertsTriggered(), "Should trigger upper breakout alert");

        // Price breaches lower threshold (145 <= 150): 2nd Alert
        market.setStockPrice("TSLA", 145.0, 300_000);
        assertEquals(2, bot.getAlertsTriggered(), "Should trigger lower breakdown alert");

        // Other symbol: Ignored
        market.setStockPrice("AMZN", 100.0, 50_000);
        assertEquals(2, bot.getAlertsTriggered(), "Should ignore different symbols");
        System.out.println("  ✓ testPriceAlertBotFiltersThresholds passed.");
    }

    private static void testPullModelMovingAverageCalculation() {
        StockMarket market = new StockMarket("NASDAQ");
        TradingAnalyticsLogger logger = new TradingAnalyticsLogger("Analytics1");
        market.register(logger);

        market.setStockPrice("GOOG", 150.0, 10_000);
        market.setStockPrice("GOOG", 160.0, 15_000);
        market.setStockPrice("GOOG", 170.0, 20_000);

        // Moving average of 150, 160, 170 is (150+160+170)/3 = 160.0
        assertEquals(160, (int) market.getMovingAverage("GOOG"), "Moving average should be 160");
        assertEquals(170, (int) market.getDailyHigh("GOOG"), "Daily high should be 170");
        assertEquals(3, logger.getCalculationsLogged(), "Logger should have pulled 3 calculations");
        System.out.println("  ✓ testPullModelMovingAverageCalculation passed.");
    }

    private static void testLapsedListenerPreventionWithAutoCloseable() {
        StockMarket market = new StockMarket("NASDAQ");

        LiveTickerDisplay widget;
        try (LiveTickerDisplay display = new LiveTickerDisplay("AutoCloseWidget", market)) {
            widget = display;
            market.setStockPrice("NVDA", 100.0, 50_000);
            assertEquals(1, widget.getTickCount(), "Widget receives tick while open");
        } // Closed here

        assertEquals(0, market.getObserverCount(), "Widget unregisters automatically on close");

        // Subsequent ticks must not notify closed widget
        market.setStockPrice("NVDA", 105.0, 60_000);
        assertEquals(1, widget.getTickCount(), "Widget tick count should remain 1 after close");
        System.out.println("  ✓ testLapsedListenerPreventionWithAutoCloseable passed.");
    }

    private static void testFaultIsolationWhenObserverThrows() {
        StockMarket market = new StockMarket("NYSE");

        StockObserver failingObserver = new StockObserver() {
            @Override
            public void onPriceUpdate(StockTick tick, StockMarket marketRef) {
                throw new RuntimeException("Simulated algorithmic panic crash!");
            }

            @Override
            public String getObserverId() {
                return "FAILING_ALGO";
            }
        };

        LiveTickerDisplay safeTicker = new LiveTickerDisplay("SafeTicker", market);
        market.register(failingObserver);

        // When tick fires, failingObserver crashes, but safeTicker must still receive update!
        market.setStockPrice("AAPL", 180.0, 100_000);

        assertEquals(1, safeTicker.getTickCount(), "Safe ticker must receive tick despite failing observer");
        safeTicker.close();
        System.out.println("  ✓ testFaultIsolationWhenObserverThrows passed.");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(String.format("Assertion failed: %s (Expected %d, got %d)",
                    message, expected, actual));
        }
    }
}
