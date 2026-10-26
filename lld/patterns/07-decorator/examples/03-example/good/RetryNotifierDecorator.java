package good;

/**
 * Concrete Decorator providing automated retry resilience with backoff.
 *
 * Responsibilities:
 * - Catches transient dispatch exceptions.
 * - Retries up to maxRetries times with configurable backoff.
 * - Re-throws the final exception if all attempts are exhausted.
 */
public class RetryNotifierDecorator extends NotifierDecorator {
    private final int maxRetries;
    private final long backoffMillis;

    public RetryNotifierDecorator(Notifier wrappee, int maxRetries, long backoffMillis) {
        super(wrappee);
        if (maxRetries < 1) {
            throw new IllegalArgumentException("maxRetries must be >= 1");
        }
        this.maxRetries = maxRetries;
        this.backoffMillis = backoffMillis;
    }

    @Override
    public void send(String recipient, String subject, String message) {
        int attempt = 0;
        while (attempt < maxRetries) {
            attempt++;
            try {
                if (attempt > 1) {
                    System.out.printf("  [RETRY] Attempt %d/%d for %s...%n", attempt, maxRetries, recipient);
                }
                super.send(recipient, subject, message);
                return; // Success, exit retry loop
            } catch (Exception e) {
                if (attempt >= maxRetries) {
                    System.err.printf("  [RETRY] Exhausted all %d attempts for %s.%n", maxRetries, recipient);
                    throw e; // Escalate final failure
                }
                System.out.printf("  [RETRY] Attempt %d failed (%s). Backing off %d ms...%n",
                        attempt, e.getMessage(), backoffMillis);
                sleep(backoffMillis);
            }
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Retry interrupted", ie);
        }
    }
}
