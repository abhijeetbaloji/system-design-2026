package bad;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * BAD DESIGN: Monolithic NotificationService tangling channel dispatch,
 * retry loops, rate limiting, and logging into a single messy class.
 *
 * Flaws:
 * 1. Violates SRP: Combines message delivery with resilience (retries),
 *    traffic policing (rate limiting), and observability (logging).
 * 2. Code Duplication: If you add SMS or Slack methods, you must duplicate
 *    the retry loop, timer, and rate limiter across each method.
 * 3. Violates OCP: Cannot add metrics or circuit-breaking without modifying
 *    this core service.
 * 4. Untestable: Cannot unit test delivery without triggering retry timers and rate limits.
 */
public class BadNotificationService {
    private final Map<String, Instant> lastSentTimestamps = new HashMap<>();
    private final int maxRetries = 3;
    private final long rateLimitWindowMs = 1000; // 1 message per second per recipient

    public void sendEmail(String recipient, String subject, String body) {
        System.out.println("[LOG] Initiating Email send to: " + recipient);
        long startTime = System.currentTimeMillis();

        // 1. Tangled Rate Limiting logic
        Instant now = Instant.now();
        Instant lastSent = lastSentTimestamps.get(recipient);
        if (lastSent != null && now.toEpochMilli() - lastSent.toEpochMilli() < rateLimitWindowMs) {
            System.err.println("[RATE LIMIT] Rejected email to " + recipient + ": rate limit exceeded!");
            return;
        }
        lastSentTimestamps.put(recipient, now);

        // 2. Tangled Retry Loop
        int attempts = 0;
        boolean success = false;
        while (attempts < maxRetries && !success) {
            attempts++;
            try {
                System.out.printf("[RETRY] Attempt %d/%d to send email...%n", attempts, maxRetries);

                // Simulate simulated network failure on first attempt
                if (attempts == 1) {
                    throw new RuntimeException("Simulated SMTP Connection Timeout");
                }

                // Actual dispatch logic buried inside resilience code
                System.out.printf(">>> [SMTP Gateway] Email dispatched to %s | Subject: %s | Body: %s%n",
                        recipient, subject, body);
                success = true;
            } catch (Exception e) {
                System.err.printf("[LOG] Attempt %d failed: %s%n", attempts, e.getMessage());
                if (attempts < maxRetries) {
                    try {
                        Thread.sleep(100); // Backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                } else {
                    System.err.println("[LOG] All retry attempts exhausted for: " + recipient);
                }
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        System.out.printf("[LOG] Operation finished in %d ms (Success: %b)%n", duration, success);
    }
}
