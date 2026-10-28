package good;

/**
 * Concrete Product: Email transport implementation.
 * Encapsulates SMTP configuration, formatting, and dispatch mechanics.
 */
public class EmailNotification implements Notification {
    private final String smtpServer;
    private final int port;

    public EmailNotification(String smtpServer, int port) {
        this.smtpServer = smtpServer;
        this.port = port;
    }

    @Override
    public void send(String recipient, String message) {
        System.out.printf("  [SMTP %s:%d] Connecting to mail transfer agent...%n", smtpServer, port);
        System.out.printf("  [SMTP] Delivering message to <%s>: %s%n", recipient, message);
    }

    @Override
    public String getChannelName() {
        return "EMAIL";
    }
}
