package bad;

/**
 * Concrete alert modal dialog.
 */
public class BadAlertBox {
    public void checkAlert(String symbol, double price) {
        if (price > 200.0) {
            System.out.printf("  [ALERT MODAL] High price alert! %s exceeded $200 (Now: $%.2f)%n", symbol, price);
        }
    }
}
