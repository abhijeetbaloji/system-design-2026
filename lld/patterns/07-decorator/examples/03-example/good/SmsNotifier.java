package good;

/**
 * Concrete Component responsible strictly for sending SMS alerts via cellular gateway.
 */
public class SmsNotifier implements Notifier {

    @Override
    public void send(String recipient, String subject, String message) {
        System.out.printf("  [SMS Gateway] >>> SMS dispatched to phone %s | %s%n",
                recipient, message);
    }
}
