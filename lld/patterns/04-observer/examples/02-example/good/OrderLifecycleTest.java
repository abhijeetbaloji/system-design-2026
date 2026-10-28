package good;

/**
 * Automated test suite for Food Delivery Order Observer implementation.
 * Verifies lifecycle notifications, observer detachment, fault isolation,
 * and duplicate prevention.
 */
public class OrderLifecycleTest {

    public static void main(String[] args) {
        System.out.println("Running OrderLifecycleTest...");

        testStatusTransitionNotifiesAllObservers();
        testDetachedObserverStopsReceivingEvents();
        testFaultIsolationWhenObserverFails();
        testDuplicateObserverPrevented();

        System.out.println("ALL 4 ORDER LIFECYCLE TESTS PASSED SUCCESSFULLY!\n");
    }

    private static void testStatusTransitionNotifiesAllObservers() {
        OrderLifecycleManager manager = new OrderLifecycleManager();
        CustomerAppNotifier customer = new CustomerAppNotifier();
        KitchenDisplaySystem kitchen = new KitchenDisplaySystem();
        DeliveryDispatchCoordinator driver = new DeliveryDispatchCoordinator();

        manager.attach(customer);
        manager.attach(kitchen);
        manager.attach(driver);

        FoodOrder order = new FoodOrder("O-100", "John Doe", "555-001", "Burger Joint", "Cheeseburger", 12.0);

        manager.transitionOrderStatus(order, OrderStatus.PREPARING);
        manager.transitionOrderStatus(order, OrderStatus.OUT_FOR_DELIVERY);
        manager.transitionOrderStatus(order, OrderStatus.DELIVERED);

        assertEquals(3, customer.getNotificationsSent(), "Customer should receive 3 status updates");
        assertEquals(1, driver.getDispatchesInitiated(), "Driver should have 1 dispatch initiated");
        System.out.println("  ✓ testStatusTransitionNotifiesAllObservers passed.");
    }

    private static void testDetachedObserverStopsReceivingEvents() {
        OrderLifecycleManager manager = new OrderLifecycleManager();
        CustomerAppNotifier customer = new CustomerAppNotifier();
        KitchenDisplaySystem kitchen = new KitchenDisplaySystem();

        manager.attach(customer);
        manager.attach(kitchen);

        FoodOrder order = new FoodOrder("O-200", "Jane", "555-002", "Salad Bar", "Caesar Salad", 9.5);

        manager.transitionOrderStatus(order, OrderStatus.PREPARING);
        assertEquals(1, customer.getNotificationsSent(), "Customer gets 1 update");

        // Customer opts out of SMS / closes app
        manager.detach(customer);
        assertEquals(1, manager.getObserverCount(), "Only kitchen remains attached");

        manager.transitionOrderStatus(order, OrderStatus.DELIVERED);
        assertEquals(1, customer.getNotificationsSent(), "Customer should NOT receive update after detaching");
        System.out.println("  ✓ testDetachedObserverStopsReceivingEvents passed.");
    }

    private static void testFaultIsolationWhenObserverFails() {
        OrderLifecycleManager manager = new OrderLifecycleManager();

        OrderObserver buggyObserver = new OrderObserver() {
            @Override
            public void onOrderStatusChanged(FoodOrder order, OrderStatus previousStatus) {
                throw new RuntimeException("Simulated SMS Gateway timeout exception!");
            }

            @Override
            public String getObserverId() {
                return "FAILING_SMS_OBSERVER";
            }
        };

        KitchenDisplaySystem kitchen = new KitchenDisplaySystem();

        manager.attach(buggyObserver);
        manager.attach(kitchen);

        FoodOrder order = new FoodOrder("O-300", "Bob", "555-003", "Sushi House", "Dragon Roll", 22.0);

        // When status transitions, failing observer must not prevent kitchen from getting ticket!
        manager.transitionOrderStatus(order, OrderStatus.PLACED);

        assertEquals(1, kitchen.getTicketCount(), "Kitchen MUST receive order ticket despite buggy observer exception");
        System.out.println("  ✓ testFaultIsolationWhenObserverFails passed.");
    }

    private static void testDuplicateObserverPrevented() {
        OrderLifecycleManager manager = new OrderLifecycleManager();
        CustomerAppNotifier customer = new CustomerAppNotifier();

        manager.attach(customer);
        manager.attach(customer); // Duplicate attempt

        assertEquals(1, manager.getObserverCount(), "Duplicate observer should not increase observer count");

        FoodOrder order = new FoodOrder("O-400", "Alice", "555-004", "Taco Stand", "3x Tacos", 10.0);
        manager.transitionOrderStatus(order, OrderStatus.PREPARING);

        assertEquals(1, customer.getNotificationsSent(), "Customer should receive exactly 1 notification, not 2");
        System.out.println("  ✓ testDuplicateObserverPrevented passed.");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(String.format("Assertion failed: %s (Expected %d, got %d)",
                    message, expected, actual));
        }
    }
}
