package good;

/**
 * Product Interface.
 * Defines the public contract for all notifications created by the Factory Method.
 *
 * The high-level Creator (NotificationService) relies ONLY on this abstraction.
 */
public interface Notification {
    /**
     * Sends the notification content to the designated recipient.
     *
     * @param recipient Target email address, phone number, etc.
     * @param message   The text payload to transmit.
     */
    void send(String recipient, String message);

    /**
     * Identifies the channel type (e.g., "EMAIL", "SMS") for logging and metrics.
     */
    String getChannelName();
}
