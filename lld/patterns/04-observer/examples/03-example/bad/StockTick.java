package bad;

/**
 * Domain representation of a stock price tick.
 */
public record StockTick(String symbol, double price, double changePercent) {}
