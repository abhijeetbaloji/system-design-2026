package bad;

public class BadNotificationDemo {
    public static void main(String[] args) {
        System.out.println("=== Bad Notification Design Demo ===");

        BadNotificationManager manager = new BadNotificationManager();

        // Dispatching email
        manager.sendAlert("EMAIL", "dev-team@company.com", "Deployment failed on production-01");

        // Dispatching SMS
        manager.sendAlert("SMS", "+15550199", "OTP code: 492811");

        System.out.println("Weaknesses exposed:");
        System.out.println("1. Adding SlackNotification requires modifying BadNotificationManager.");
        System.out.println("2. The manager directly manages low-level SMTP and telecom gateway details.");
        System.out.println("3. Type codes ('EMAIL', 'SMS') lack compile-time type safety.");
    }
}
