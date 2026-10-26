package good;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Concrete Decorator providing operational metrics collection.
 *
 * Responsibilities:
 * - Records cumulative invocation counts, success rates, and failure tallies.
 * - Conforms strictly to Notifier while offering telemetry accessors for monitoring dashboards.
 */
public class MetricsNotifierDecorator extends NotifierDecorator {
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong successfulRequests = new AtomicLong(0);
    private final AtomicLong failedRequests = new AtomicLong(0);

    public MetricsNotifierDecorator(Notifier wrappee) {
        super(wrappee);
    }

    @Override
    public void send(String recipient, String subject, String message) {
        totalRequests.incrementAndGet();
        try {
            super.send(recipient, subject, message);
            successfulRequests.incrementAndGet();
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            throw e;
        }
    }

    public long getTotalRequests() { return totalRequests.get(); }
    public long getSuccessfulRequests() { return successfulRequests.get(); }
    public long getFailedRequests() { return failedRequests.get(); }

    public void printReport() {
        System.out.printf("%n--- Telemetry Metrics Summary ---%n"
                        + "Total Invocations: %d%n"
                        + "Successful:        %d%n"
                        + "Failed:            %d%n"
                        + "---------------------------------%n",
                totalRequests.get(), successfulRequests.get(), failedRequests.get());
    }
}
