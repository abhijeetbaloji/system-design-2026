package good;

/**
 * Client Demonstration.
 *
 * Demonstrates:
 * 1. Attaching Customer, Kitchen, and Driver observers to OrderLifecycleManager.
 * 2. Walking an order through its full lifecycle: PLACED -> PREPARING -> OUT_FOR_DELIVERY -> DELIVERED.
 * 3. Shows each observer independently reacting to each transition.
 */
public class FoodDeliveryDemo {

    public static void main(String[] args) {
        System.out.println("=== Factory & Observer Patterns: Food Delivery Order Tracking Demo ===\n");

        OrderLifecycleManager orderManager = new OrderLifecycleManager();

        // Instantiate observers
        OrderObserver customerNotifier = new CustomerAppNotifier();
        OrderObserver kitchenDisplay = new KitchenDisplaySystem();
        OrderObserver driverCoordinator = new DeliveryDispatchCoordinator();

        // Attach observers
        orderManager.attach(customerNotifier);
        orderManager.attach(kitchenDisplay);
        orderManager.attach(driverCoordinator);

        // Create an order (initially in PLACED state)
        FoodOrder order = new FoodOrder(
                "ORD-9901",
                "Sophia Chen",
                "+1-415-555-8822",
                "Bella Italia Trattoria",
                "1x Margherita Pizza, 1x Tiramisu",
                38.50
        );

        // Notify initial placement
        orderManager.notifyObservers(order, OrderStatus.PLACED);

        // Order Lifecycle Transitions
        orderManager.transitionOrderStatus(order, OrderStatus.PREPARING);
        orderManager.transitionOrderStatus(order, OrderStatus.OUT_FOR_DELIVERY);
        orderManager.transitionOrderStatus(order, OrderStatus.DELIVERED);

        System.out.printf("%nExecution Complete: Order #%s reached terminal state: %s.%n",
                order.getOrderId(), order.getStatus());
    }
}
