package good;

/**
 * Creator (Abstract Class).
 *
 * This class contains the CORE BUSINESS LOGIC for alerts. It is intentionally
 * decoupled from concrete notification implementations.
 *
 * When an alert must be sent, it calls the abstract Factory Method:
 *   {@link #createNotification()}
 *
 * Subclasses will override this factory method to supply the appropriate
 * transport product (EmailNotification, SmsNotification, etc.).
 */
public abstract class NotificationService {

    /**
     * Core business workflow (Template Method).
     * Defines the standard multi-step alert dispatch procedure.
     */
    public void sendAlert(String recipient, String message) {
        // Step 1: Centralized validation
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient cannot be null or empty.");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Alert message cannot be null or empty.");
        }

        // Step 2: Call the Factory Method to obtain the appropriate product.
        // Notice: The creator has no idea whether this is an email, SMS, or smoke signal!
        Notification notification = createNotification();

        System.out.printf("[%s Service] Initializing alert workflow...%n", notification.getChannelName());

        // Step 3: Perform the core operation using the Product abstraction
        notification.send(recipient, message);

        // Step 4: Centralized post-dispatch auditing & telemetry
        recordAuditTrail(recipient, notification.getChannelName());
    }

    /**
     * Standard post-dispatch audit step. Shared across all channels.
     */
    private void recordAuditTrail(String recipient, String channel) {
        System.out.printf("[Audit] Alert successfully processed for target '%s' via %s.%n%n", recipient, channel);
    }

    /**
     * THE FACTORY METHOD.
     * Subclasses must override this to return a specific Concrete Product.
     */
    protected abstract Notification createNotification();
}
