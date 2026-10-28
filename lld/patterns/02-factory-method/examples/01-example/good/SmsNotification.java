package good;

/**
 * Concrete Product: SMS transport implementation.
 * Encapsulates SMS telecom carrier interactions, character chunking, and routing.
 */
public class SmsNotification implements Notification {
    private final String senderId;

    public SmsNotification(String senderId) {
        this.senderId = senderId;
    }

    @Override
    public void send(String recipient, String message) {
        System.out.printf("  [SMS Gateway: %s] Routing text packet to mobile number: %s%n", senderId, recipient);
        System.out.printf("  [SMS Gateway] Content: \"%s\"%n", message);
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }
}
