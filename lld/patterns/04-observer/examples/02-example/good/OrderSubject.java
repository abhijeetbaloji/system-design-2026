package good;

/**
 * Subject Interface for Order Lifecycle events.
 */
public interface OrderSubject {

    /**
     * Attaches an observer to receive order status updates.
     */
    void attach(OrderObserver observer);

    /**
     * Detaches an observer so it no longer receives updates.
     */
    void detach(OrderObserver observer);

    /**
     * Broadcasts an order status transition to all attached observers.
     */
    void notifyObservers(FoodOrder order, OrderStatus previousStatus);

    /**
     * Returns the number of currently attached observers.
     */
    int getObserverCount();
}
