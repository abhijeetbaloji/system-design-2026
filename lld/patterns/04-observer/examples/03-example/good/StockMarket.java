package good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Concrete Subject: StockMarket.
 *
 * Maintains real-time financial market prices and broadcasts updates to observers.
 * Provides analytical query endpoints supporting the PULL model.
 */
public class StockMarket implements StockMarketSubject {
    private final String exchangeName;
    private final List<StockObserver> observers = new CopyOnWriteArrayList<>();
    private final Map<String, List<Double>> priceHistory = new ConcurrentHashMap<>();
    private final Map<String, Double> currentPrices = new ConcurrentHashMap<>();

    public StockMarket(String exchangeName) {
        if (exchangeName == null || exchangeName.isBlank()) {
            throw new IllegalArgumentException("Exchange name cannot be blank.");
        }
        this.exchangeName = exchangeName;
    }

    @Override
    public void register(StockObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null.");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.printf("[%s] Registered observer: %s (Total observers: %d)%n",
                    exchangeName, observer.getObserverId(), observers.size());
        } else {
            System.out.printf("[%s] Observer %s already registered. Duplicate ignored.%n",
                    exchangeName, observer.getObserverId());
        }
    }

    @Override
    public void unregister(StockObserver observer) {
        if (observer != null && observers.remove(observer)) {
            System.out.printf("[%s] Unregistered observer: %s (Total observers: %d)%n",
                    exchangeName, observer.getObserverId(), observers.size());
        }
    }

    @Override
    public void notifyObservers(StockTick tick) {
        for (StockObserver observer : observers) {
            try {
                // Passes lightweight tick (PUSH) + market reference (PULL)
                observer.onPriceUpdate(tick, this);
            } catch (Exception ex) {
                // Fault isolation: bad observer doesn't bring down trading engine
                System.err.printf("  [WARN] Observer '%s' threw exception: %s%n",
                        observer.getObserverId(), ex.getMessage());
            }
        }
    }

    /**
     * Publishes a new price update for a given ticker symbol.
     */
    public void setStockPrice(String symbol, double newPrice, long volume) {
        if (symbol == null || symbol.isBlank() || newPrice <= 0) {
            throw new IllegalArgumentException("Invalid stock symbol or price.");
        }

        double oldPrice = currentPrices.getOrDefault(symbol, newPrice);
        double changePercent = oldPrice == 0.0 ? 0.0 : ((newPrice - oldPrice) / oldPrice) * 100.0;

        currentPrices.put(symbol, newPrice);
        priceHistory.computeIfAbsent(symbol, k -> new CopyOnWriteArrayList<>()).add(newPrice);

        StockTick tick = new StockTick(symbol, newPrice, changePercent, volume);
        System.out.printf("%n=== [%s Broadcast] %s Tick: $%.2f (%+.2f%%) ===%n",
                exchangeName, symbol, newPrice, changePercent);

        notifyObservers(tick);
    }

    // --- PULL MODEL ENDPOINTS FOR OBSERVERS ---

    /**
     * PULL method: Computes the simple moving average over recent ticks for a symbol.
     */
    public double getMovingAverage(String symbol) {
        List<Double> history = priceHistory.getOrDefault(symbol, Collections.emptyList());
        if (history.isEmpty()) return 0.0;
        int window = Math.min(history.size(), 5);
        double sum = 0.0;
        for (int i = history.size() - window; i < history.size(); i++) {
            sum += history.get(i);
        }
        return sum / window;
    }

    /**
     * PULL method: Returns the session high for a symbol.
     */
    public double getDailyHigh(String symbol) {
        List<Double> history = priceHistory.getOrDefault(symbol, Collections.emptyList());
        return history.stream().max(Double::compareTo).orElse(0.0);
    }

    @Override
    public int getObserverCount() {
        return observers.size();
    }

    public String getExchangeName() {
        return exchangeName;
    }
}
