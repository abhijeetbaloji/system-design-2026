package good;

/**
 * Concrete Observer: Trading Analytics Logger.
 *
 * Demonstrates the PULL MODEL:
 * Upon receiving a price notification, it queries the {@link StockMarket} subject
 * reference to PULL heavy computational statistics (moving average, high/low spread)
 * without bloating the original event notification payload for other observers.
 */
public class TradingAnalyticsLogger implements StockObserver {
    private final String loggerId;
    private int calculationsLogged = 0;

    public TradingAnalyticsLogger(String loggerId) {
        this.loggerId = loggerId;
    }

    @Override
    public void onPriceUpdate(StockTick tick, StockMarket market) {
        // PULL: Selectively pulls analytics data from the market subject
        double movingAverage = market.getMovingAverage(tick.symbol());
        double dailyHigh = market.getDailyHigh(tick.symbol());
        calculationsLogged++;

        System.out.printf("  [Analytics Logger: %s] Pulled stats for %s -> 5-Tick SMA: $%.2f | Session High: $%.2f%n",
                loggerId, tick.symbol(), movingAverage, dailyHigh);
    }

    @Override
    public String getObserverId() {
        return loggerId;
    }

    public int getCalculationsLogged() {
        return calculationsLogged;
    }
}
