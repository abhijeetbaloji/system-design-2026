package good;

/**
 * Observer Interface for Stock Market updates.
 *
 * Supports both PUSH and PULL models:
 * - PUSH: The StockTick payload provides immediate price and percentage change.
 * - PULL: The StockMarket reference enables observers to pull additional heavy
 *   metrics (e.g. moving average, 24h volume) only when needed.
 */
public interface StockObserver {

    /**
     * Callback invoked when a stock price changes.
     *
     * @param tick   Lightweight pushed event data.
     * @param market Reference to the Subject for pulling supplemental data.
     */
    void onPriceUpdate(StockTick tick, StockMarket market);

    /**
     * Unique identifier for this observer instance.
     */
    String getObserverId();
}
