package good;

/**
 * Observer Interface.
 *
 * Defines the contract for all listeners observing order status lifecycle changes.
 */
public interface OrderObserver {

    /**
     * Callback invoked when an order changes status.
     *
     * @param order          The order containing updated status and details.
     * @param previousStatus The status of the order before the transition.
     */
    void onOrderStatusChanged(FoodOrder order, OrderStatus previousStatus);

    /**
     * Unique identifier for the observer (e.g., "CUSTOMER_APP", "KITCHEN_DISPLAY").
     */
    String getObserverId();
}
