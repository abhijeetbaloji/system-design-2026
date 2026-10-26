package good;

/**
 * Component interface.
 * Defines the contract for sending notifications.
 *
 * Core channel delivery agents (Email, Slack, SMS) and operational
 * enhancements (Logging, Retry, Rate Limiting, Metrics) all implement this interface.
 */
public interface Notifier {
    void send(String recipient, String subject, String message);
}
