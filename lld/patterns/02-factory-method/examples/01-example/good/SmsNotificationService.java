package good;

/**
 * Concrete Creator: SmsNotificationService.
 *
 * Overrides the factory method {@link #createNotification()} to instantiate
 * and configure an {@link SmsNotification}.
 */
public class SmsNotificationService extends NotificationService {
    private final String senderId;

    public SmsNotificationService() {
        this("ENTERPRISE-ALERT");
    }

    public SmsNotificationService(String senderId) {
        this.senderId = senderId;
    }

    @Override
    protected Notification createNotification() {
        // Encapsulates configuration and instantiation of SmsNotification
        return new SmsNotification(senderId);
    }
}
