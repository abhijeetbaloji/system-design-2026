package good;

/**
 * Concrete Observer: Customer Notification System.
 * Translates order state transitions into customer-friendly push notifications and SMS alerts.
 */
public class CustomerAppNotifier implements OrderObserver {
    private int notificationsSent = 0;

    @Override
    public void onOrderStatusChanged(FoodOrder order, OrderStatus previousStatus) {
        notificationsSent++;
        String message = switch (order.getStatus()) {
            case PLACED -> "Your order #" + order.getOrderId() + " has been received by " + order.getRestaurantName() + "!";
            case PREPARING -> "Chef at " + order.getRestaurantName() + " started preparing your food.";
            case OUT_FOR_DELIVERY -> "Driver has picked up your food and is on the way!";
            case DELIVERED -> "Your food has arrived! Enjoy your meal.";
            case CANCELLED -> "Your order #" + order.getOrderId() + " was cancelled. Refund initiated.";
        };

        System.out.printf("  [Customer App Push -> %s (%s)] %s%n",
                order.getCustomerName(), order.getCustomerPhone(), message);
    }

    @Override
    public String getObserverId() {
        return "CUSTOMER_APP_NOTIFIER";
    }

    public int getNotificationsSent() {
        return notificationsSent;
    }
}
