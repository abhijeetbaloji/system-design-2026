package bad;

/**
 * Concrete telecom SMS gateway helper.
 */
public class CustomerSmsGateway {
    public void sendStatusSms(String customerPhone, String message) {
        System.out.printf("  [SMS Gateway -> %s] %s%n", customerPhone, message);
    }
}
