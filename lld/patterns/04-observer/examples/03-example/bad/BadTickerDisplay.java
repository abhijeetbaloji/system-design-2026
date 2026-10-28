package bad;

/**
 * Concrete desktop GUI ticker display widget.
 */
public class BadTickerDisplay {
    private boolean isClosed = false;

    public void updateTicker(String symbol, double price, double change) {
        if (isClosed) {
            // Flaw: Stale widget still receives updates because subject holds strong reference!
            System.out.printf("  [LEAK WARNING] BadTickerDisplay is CLOSED, but still receiving tick: %s @ $%.2f!%n",
                    symbol, price);
            return;
        }
        System.out.printf("  [Live Ticker] %s: $%.2f (%+.2f%%)%n", symbol, price, change);
    }

    public void closeWidget() {
        this.isClosed = true;
    }
}
