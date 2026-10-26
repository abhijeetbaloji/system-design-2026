package good;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Concrete Component responsible strictly for Email dispatch via SMTP.
 *
 * It contains zero retry loops, logging frameworks, or rate limiters.
 * Includes a simulated failure counter to demonstrate transient network glitches.
 */
public class EmailNotifier implements Notifier {
    private final AtomicInteger transientFailureCountdown;

    public EmailNotifier() {
        this(0);
    }

    public EmailNotifier(int simulateInitialFailures) {
        this.transientFailureCountdown = new AtomicInteger(simulateInitialFailures);
    }

    @Override
    public void send(String recipient, String subject, String message) {
        if (transientFailureCountdown.getAndDecrement() > 0) {
            throw new RuntimeException("Transient SMTP Gateway 503: Connection Timeout");
        }
        System.out.printf("  [SMTP Gateway] >>> Email sent to %s | Subject: %s | Message: %s%n",
                recipient, subject, message);
    }
}
