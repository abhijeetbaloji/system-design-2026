package good;

/**
 * Concrete Creator: EmailNotificationService.
 *
 * Overrides the factory method {@link #createNotification()} to instantiate
 * and configure an {@link EmailNotification}.
 */
public class EmailNotificationService extends NotificationService {
    private final String smtpHost;
    private final int smtpPort;

    public EmailNotificationService() {
        this("smtp.production.internal", 587);
    }

    public EmailNotificationService(String smtpHost, int smtpPort) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    @Override
    protected Notification createNotification() {
        // Encapsulates configuration and instantiation of EmailNotification
        return new EmailNotification(smtpHost, smtpPort);
    }
}
