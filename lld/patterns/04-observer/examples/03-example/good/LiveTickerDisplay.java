package good;

/**
 * Concrete Observer: Real-time Live Ticker Display widget.
 * Implements AutoCloseable to guarantee safe unsubscription and prevent memory leaks.
 */
public class LiveTickerDisplay implements StockObserver, AutoCloseable {
    private final String widgetId;
    private final StockMarketSubject market;
    private int tickCount = 0;
    private boolean active = true;

    public LiveTickerDisplay(String widgetId, StockMarketSubject market) {
        this.widgetId = widgetId;
        this.market = market;
        this.market.register(this);
    }

    @Override
    public void onPriceUpdate(StockTick tick, StockMarket marketRef) {
        if (!active) {
            throw new IllegalStateException("Display widget is closed but received an update!");
        }
        tickCount++;
        System.out.printf("  [Live Ticker: %s] %s: $%.2f (%+.2f%% | Vol: %,d)%n",
                widgetId, tick.symbol(), tick.price(), tick.changePercent(), tick.volume());
    }

    @Override
    public void close() {
        if (active) {
            active = false;
            // Clean unsubscription: removes reference from subject
            market.unregister(this);
            System.out.printf("[Widget %s] Closed cleanly and unregistered from market feed.%n", widgetId);
        }
    }

    @Override
    public String getObserverId() {
        return widgetId;
    }

    public int getTickCount() {
        return tickCount;
    }

    public boolean isActive() {
        return active;
    }
}
