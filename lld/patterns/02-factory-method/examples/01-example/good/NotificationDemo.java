package good;

/**
 * Client Demonstration.
 *
 * Demonstrates how client code programs against the Creator abstraction
 * (NotificationService). Switching channels does not alter the alert dispatch logic.
 */
public class NotificationDemo {

    public static void main(String[] args) {
        System.out.println("=== Factory Method Pattern: Notification System Demo ===\n");

        // Client chooses the desired service variant (or receives it via dependency injection)
        NotificationService emailService = new EmailNotificationService("mail.cluster.internal", 465);
        NotificationService smsService = new SmsNotificationService("AWS-SNS-ALERT");

        // The client interacts strictly through the high-level business workflow
        triggerSystemEvent(emailService, "admin@cloud.internal", "Database replication lag > 5000ms");
        triggerSystemEvent(smsService, "+1-415-555-2671", "Critical: High memory on node-4");
    }

    /**
     * Polymorphic client method.
     * Works with any NotificationService subclass without knowing concrete details.
     */
    private static void triggerSystemEvent(NotificationService service, String recipient, String message) {
        service.sendAlert(recipient, message);
    }
}
