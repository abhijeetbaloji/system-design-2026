package good;

/**
 * Concrete Observer: Filtered Price Alert Bot.
 * Demonstrates selective notification: ignores ordinary micro-fluctuations and
 * only reacts when a stock crosses specified price boundaries.
 */
public class PriceAlertBot implements StockObserver {
    private final String botName;
    private final String targetSymbol;
    private final double upperThreshold;
    private final double lowerThreshold;
    private int alertsTriggered = 0;

    public PriceAlertBot(String botName, String targetSymbol, double lowerThreshold, double upperThreshold) {
        this.botName = botName;
        this.targetSymbol = targetSymbol;
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;
    }

    @Override
    public void onPriceUpdate(StockTick tick, StockMarket market) {
        if (!tick.symbol().equalsIgnoreCase(targetSymbol)) {
            return; // Ignore other ticker symbols
        }

        if (tick.price() >= upperThreshold) {
            alertsTriggered++;
            System.out.printf("  [ALERT BOT: %s] *** BULLISH BREAKOUT *** %s hit $%.2f (>= $%.2f)!%n",
                    botName, targetSymbol, tick.price(), upperThreshold);
        } else if (tick.price() <= lowerThreshold) {
            alertsTriggered++;
            System.out.printf("  [ALERT BOT: %s] *** BEARISH DROP *** %s fell to $%.2f (<= $%.2f)!%n",
                    botName, targetSymbol, tick.price(), lowerThreshold);
        }
    }

    @Override
    public String getObserverId() {
        return botName;
    }

    public int getAlertsTriggered() {
        return alertsTriggered;
    }
}
