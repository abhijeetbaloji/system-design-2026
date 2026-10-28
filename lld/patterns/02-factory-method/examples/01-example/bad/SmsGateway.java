package bad;

/**
 * Concrete helper for SMS transmission.
 * Represents another low-level telecom transport dependency with unique APIs.
 */
public class SmsGateway {
    private final String senderId;

    public SmsGateway(String senderId) {
        this.senderId = senderId;
    }

    public void transmitText(String phoneNumber, String text) {
        System.out.printf("[Telecom Sender: %s] Transmitting SMS to: %s%n", senderId, phoneNumber);
        System.out.printf("  Message: %s%n", text);
    }
}
