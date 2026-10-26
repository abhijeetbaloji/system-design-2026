package good;

/**
 * Concrete Decorator providing structured observability logging.
 *
 * Responsibilities:
 * - Logs notification attempts, target recipients, elapsed dispatch time,
 *   and success or failure outcomes.
 */
public class LoggingNotifierDecorator extends NotifierDecorator {

    public LoggingNotifierDecorator(Notifier wrappee) {
        super(wrappee);
    }

    @Override
    public void send(String recipient, String subject, String message) {
        System.out.printf("[LOG] Starting dispatch to: %s | Subject: '%s'%n", recipient, subject);
        long startTime = System.currentTimeMillis();
        try {
            super.send(recipient, subject, message);
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.printf("[LOG] Successfully delivered to %s in %d ms.%n", recipient, elapsed);
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - startTime;
            System.err.printf("[LOG] Delivery to %s failed after %d ms. Reason: %s%n",
                    recipient, elapsed, e.getMessage());
            throw e;
        }
    }
}
