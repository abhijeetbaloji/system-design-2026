package good;

/**
 * Concrete Component responsible strictly for sending messages via Slack Webhooks.
 */
public class SlackNotifier implements Notifier {
    private final String webhookUrl;

    public SlackNotifier(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @Override
    public void send(String recipient, String subject, String message) {
        System.out.printf("  [Slack Webhook: %s] >>> Channel #%s | %s: %s%n",
                webhookUrl, recipient, subject, message);
    }
}
