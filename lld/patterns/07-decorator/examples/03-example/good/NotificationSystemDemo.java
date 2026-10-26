package good;

public class NotificationSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== Enterprise Notification Pipeline - Decorator Pattern Demo ===\n");

        // --- Scenario 1: Resilient Email Pipeline ---
        System.out.println("--- Scenario 1: Email Pipeline with Retry, Rate Limiting, Logging & Metrics ---");

        // Simulate an EmailNotifier that fails once due to a transient network glitch, then succeeds
        Notifier coreEmail = new EmailNotifier(1);

        // Assemble decorator pipeline:
        // Core -> Retry -> RateLimiter -> Logging -> Metrics
        MetricsNotifierDecorator emailPipeline = new MetricsNotifierDecorator(
                new LoggingNotifierDecorator(
                        new RateLimitingNotifierDecorator(
                                new RetryNotifierDecorator(coreEmail, 3, 100),
                                1000 // 1-second rate limit window per recipient
                        )
                )
        );

        // 1. First send: Should trigger retry under the hood and succeed transparently
        System.out.println("Dispatching notification 1 (triggers retry, then succeeds):");
        emailPipeline.send("dev-team@company.com", "High CPU Alert", "CPU utilization exceeded 90% on node-04");

        // 2. Immediate second send to same recipient: Should be intercepted and dropped by RateLimiter
        System.out.println("\nDispatching rapid notification 2 to same recipient (intercepted by rate limiter):");
        emailPipeline.send("dev-team@company.com", "High Memory Alert", "RAM usage exceeded 85% on node-04");

        // --- Scenario 2: Reusing the exact same decorator stack for Slack ---
        System.out.println("\n--- Scenario 2: Reusing Resilience Stack with SlackNotifier (Zero Code Duplication) ---");

        Notifier coreSlack = new SlackNotifier("https://hooks.slack.com/services/T00/B00/X00");

        // The identical Logging and Retry wrappers decorate Slack without duplicating any resilience code
        Notifier slackPipeline = new LoggingNotifierDecorator(
                new RetryNotifierDecorator(coreSlack, 3, 100)
        );

        slackPipeline.send("ops-war-room", "Deployment Status", "Release v2.4.0 successfully deployed to production.");

        // --- Scenario 3: Telemetry Report ---
        emailPipeline.printReport();
    }
}
