package bad;

/**
 * Concrete helper for email transmission.
 * Represents a concrete low-level transport dependency.
 */
public class EmailService {
    private final String host;

    public EmailService(String host) {
        this.host = host;
    }

    public void sendEmail(String emailAddress, String body) {
        System.out.printf("[SMTP Host: %s] Sending email to: %s%n", host, emailAddress);
        System.out.printf("  Body: %s%n", body);
    }
}
