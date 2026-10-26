package good;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concrete Decorator providing recipient-level rate limiting.
 *
 * Responsibilities:
 * - Prevents spamming recipients by enforcing a minimum cool-down window.
 * - Rejects or drops requests that exceed the permitted threshold without
 *   burdening downstream gateways or notification logic.
 */
public class RateLimitingNotifierDecorator extends NotifierDecorator {
    private final long minIntervalMillis;
    private final Map<String, Instant> lastDispatchTimes = new ConcurrentHashMap<>();

    public RateLimitingNotifierDecorator(Notifier wrappee, long minIntervalMillis) {
        super(wrappee);
        this.minIntervalMillis = minIntervalMillis;
    }

    @Override
    public void send(String recipient, String subject, String message) {
        Instant now = Instant.now();
        Instant lastSent = lastDispatchTimes.get(recipient);

        if (lastSent != null && (now.toEpochMilli() - lastSent.toEpochMilli() < minIntervalMillis)) {
            long remaining = minIntervalMillis - (now.toEpochMilli() - lastSent.toEpochMilli());
            System.err.printf("[RATE LIMIT] Rejected message to '%s': Cool-down active (%d ms remaining).%n",
                    recipient, remaining);
            return; // Dropped by rate limiter policy
        }

        lastDispatchTimes.put(recipient, now);
        super.send(recipient, subject, message);
    }
}
