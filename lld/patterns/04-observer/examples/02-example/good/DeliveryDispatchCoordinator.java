package good;

/**
 * Concrete Observer: Delivery Driver Dispatch Coordinator.
 * Manages driver allocation, dispatching, and route tracking based on order status.
 */
public class DeliveryDispatchCoordinator implements OrderObserver {
    private int dispatchesInitiated = 0;

    @Override
    public void onOrderStatusChanged(FoodOrder order, OrderStatus previousStatus) {
        switch (order.getStatus()) {
            case PLACED -> {
                // Pre-warming driver matching pool in the restaurant neighborhood
                System.out.printf("  [Driver Dispatch] Monitoring fleet near %s for order #%s%n",
                        order.getRestaurantName(), order.getOrderId());
            }
            case PREPARING -> {
                dispatchesInitiated++;
                System.out.printf("  [Driver Dispatch] Assigned courier Alex (Toyota Prius) to order #%s at %s%n",
                        order.getOrderId(), order.getRestaurantName());
            }
            case OUT_FOR_DELIVERY -> {
                System.out.printf("  [Driver Dispatch] Courier in transit with order #%s to %s%n",
                        order.getOrderId(), order.getCustomerName());
            }
            case DELIVERED -> {
                System.out.printf("  [Driver Dispatch] Courier completed order #%s. Driver released to idle pool.%n",
                        order.getOrderId());
            }
            case CANCELLED -> {
                System.out.printf("  [Driver Dispatch] Order #%s cancelled. Releasing assigned courier.%n",
                        order.getOrderId());
            }
        }
    }

    @Override
    public String getObserverId() {
        return "DRIVER_DISPATCH_COORDINATOR";
    }

    public int getDispatchesInitiated() {
        return dispatchesInitiated;
    }
}
