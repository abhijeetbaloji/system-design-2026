package good;

/**
 * Domain record representing a financial stock market price tick.
 */
public record StockTick(String symbol, double price, double changePercent, long volume) {
    public StockTick {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Stock symbol must not be blank.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Stock price must be strictly positive.");
        }
    }
}
