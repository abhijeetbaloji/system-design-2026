package good;

/**
 * Subject Interface for Financial Market Data broadcasts.
 */
public interface StockMarketSubject {

    /**
     * Registers a financial observer to the price broadcast feed.
     */
    void register(StockObserver observer);

    /**
     * Unregisters an observer so it ceases receiving updates.
     */
    void unregister(StockObserver observer);

    /**
     * Broadcasts price tick event to all registered observers.
     */
    void notifyObservers(StockTick tick);

    /**
     * Returns the active observer count.
     */
    int getObserverCount();
}
