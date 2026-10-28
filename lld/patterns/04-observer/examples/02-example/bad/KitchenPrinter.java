package bad;

/**
 * Concrete thermal receipt printer in the restaurant kitchen.
 */
public class KitchenPrinter {
    public void printOrderTicket(String orderId, String restaurant, String items) {
        System.out.printf("  [Kitchen Thermal Printer] *** TICKET #%s ***%n", orderId);
        System.out.printf("  [Kitchen] Restaurant: %s | Items: %s%n", restaurant, items);
    }

    public void updateKitchenTicketStatus(String orderId, String status) {
        System.out.printf("  [Kitchen Display] Ticket #%s status updated to: %s%n", orderId, status);
    }
}
