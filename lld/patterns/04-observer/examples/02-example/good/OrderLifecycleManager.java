package good;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Concrete Subject: OrderLifecycleManager.
 *
 * Coordinates the food delivery order lifecycle.
 * Maintains registered observers and broadcasts order status updates.
 *
 * Robustness guarantees:
 * 1. Thread-safe observer collection using CopyOnWriteArrayList.
 * 2. Idempotent attachment: duplicate observers are ignored.
 * 3. Fault isolation: if an observer throws an unexpected exception,
 *    other observers still receive the notification.
 */
public class OrderLifecycleManager implements OrderSubject {
    private final List<OrderObserver> observers = new CopyOnWriteArrayList<>();

    @Override
    public void attach(OrderObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null.");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.printf("[OrderManager] Attached observer: %s (Total observers: %d)%n",
                    observer.getObserverId(), observers.size());
        } else {
            System.out.printf("[OrderManager] Observer %s is already attached. Ignored duplicate.%n",
                    observer.getObserverId());
        }
    }

    @Override
    public void detach(OrderObserver observer) {
        if (observer != null && observers.remove(observer)) {
            System.out.printf("[OrderManager] Detached observer: %s (Total observers: %d)%n",
                    observer.getObserverId(), observers.size());
        }
    }

    @Override
    public void notifyObservers(FoodOrder order, OrderStatus previousStatus) {
        System.out.printf("%n--- [Order Lifecycle Event] Order #%s: %s -> %s ---%n",
                order.getOrderId(), previousStatus, order.getStatus());

        for (OrderObserver observer : observers) {
            try {
                observer.onOrderStatusChanged(order, previousStatus);
            } catch (Exception ex) {
                // Fault isolation: one failing observer does not halt the notification chain
                System.err.printf("  [WARN] Observer '%s' failed to process update: %s%n",
                        observer.getObserverId(), ex.getMessage());
            }
        }
    }

    /**
     * Business method: Transitions the order to a new status and triggers notifications.
     */
    public void transitionOrderStatus(FoodOrder order, OrderStatus newStatus) {
        if (order == null || newStatus == null) {
            throw new IllegalArgumentException("Order and newStatus must not be null.");
        }
        OrderStatus previous = order.getStatus();
        order.setStatus(newStatus);
        notifyObservers(order, previous);
    }

    @Override
    public int getObserverCount() {
        return observers.size();
    }
}
