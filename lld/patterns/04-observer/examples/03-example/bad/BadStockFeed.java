package bad;

/**
 * BAD DESIGN: Monolithic stock feed with hardcoded UI widgets.
 *
 * Flaws exposed:
 * 1. Lapsed Listener Memory Leak: The feed retains hardcoded references to UI panels
 *    even after they are closed, continuously invoking methods on dead objects.
 * 2. Violates Open/Closed Principle (OCP): Adding an algorithmic trader or database
 *    archiver requires editing BadStockFeed and changing the update loop.
 * 3. Inflexible Notification: Pushes arbitrary parameters to different methods
 *    without a shared abstraction.
 */
public class BadStockFeed {
    private final BadTickerDisplay tickerDisplay = new BadTickerDisplay();
    private final BadAlertBox alertBox = new BadAlertBox();

    public void publishTick(StockTick tick) {
        System.out.printf("%n[Stock Exchange Feed] Processing tick for %s...%n", tick.symbol());

        // Hardcoded invocations
        tickerDisplay.updateTicker(tick.symbol(), tick.price(), tick.changePercent());
        alertBox.checkAlert(tick.symbol(), tick.price());
    }

    public BadTickerDisplay getTickerDisplay() {
        return tickerDisplay;
    }
}
