package bad;

/**
 * Concrete driver dispatch coordination service.
 */
public class DriverDispatchService {
    public void assignDriver(String orderId) {
        System.out.printf("  [Driver Dispatch] Driver assigned for order #%s%n", orderId);
    }

    public void completeDelivery(String orderId) {
        System.out.printf("  [Driver Dispatch] Marked delivery complete for order #%s%n", orderId);
    }
}
