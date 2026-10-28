package bad;

/**
 * BAD DESIGN: Food delivery service directly coordinating all status changes.
 *
 * Flaws exposed:
 * 1. Violates Open/Closed Principle (OCP): Adding an in-app push service, analytics,
 *    or billing auditor requires modifying this class.
 * 2. Violates Dependency Inversion Principle (DIP): Directly instantiates concrete
 *    telecom, kitchen, and driver hardware/software services.
 * 3. Cascade Failures: If CustomerSmsGateway throws an unexpected network error,
 *    the entire updateStatus method crashes, preventing the kitchen printer from running!
 * 4. High Coupling: Order state logic is hopelessly intermingled with dispatch mechanics.
 */
public class BadOrderService {
    // Hardcoded dependencies
    private final CustomerSmsGateway smsGateway = new CustomerSmsGateway();
    private final KitchenPrinter kitchenPrinter = new KitchenPrinter();
    private final DriverDispatchService driverService = new DriverDispatchService();

    public void updateStatus(String orderId, String customerPhone, String restaurant, String items, OrderStatus newStatus) {
        System.out.printf("%n[Order #%s] Status transitioned to: %s%n", orderId, newStatus);

        // FLAW: Massive switch with hardcoded notifications to disparate classes
        switch (newStatus) {
            case PLACED -> {
                smsGateway.sendStatusSms(customerPhone, "Your order #" + orderId + " has been placed!");
                kitchenPrinter.printOrderTicket(orderId, restaurant, items);
            }
            case PREPARING -> {
                smsGateway.sendStatusSms(customerPhone, "Chef is now preparing your food!");
                kitchenPrinter.updateKitchenTicketStatus(orderId, "COOKING");
                driverService.assignDriver(orderId);
            }
            case OUT_FOR_DELIVERY -> {
                smsGateway.sendStatusSms(customerPhone, "Your driver is heading your way!");
                kitchenPrinter.updateKitchenTicketStatus(orderId, "PICKED_UP");
            }
            case DELIVERED -> {
                smsGateway.sendStatusSms(customerPhone, "Enjoy your meal! Delivered successfully.");
                driverService.completeDelivery(orderId);
            }
        }
    }
}
