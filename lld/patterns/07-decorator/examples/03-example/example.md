# Example 03: Notification Enhancements (Logging, Rate-Limiting, Retry, Metrics)

## 1. The Problem

Enterprise applications communicate with users and operators through multiple notification channels:
- **Email:** Dispatched via SMTP gateways.
- **Slack:** Dispatched via incoming webhooks.
- **SMS:** Dispatched via cellular SMS aggregators.

Each of these outbound communication channels requires identical operational enhancements:
1. **Resilience (Retries with backoff):** Recovering from transient network timeouts without failing client business workflows.
2. **Traffic Policing (Rate Limiting):** Preventing accidental spam or denial of service by enforcing rate limits per recipient.
3. **Observability (Logging):** Tracing dispatch latency and delivery outcomes.
4. **Telemetry (Metrics):** Tracking success rates and error budgets.

---

## 2. Bad Design Analysis

### Flaw: The Monolithic Channel Service (`bad/BadNotificationService.java`)
In traditional ad-hoc designs, developers embed resilience and logging directly inside each channel method:

```java
public class BadNotificationService {
    public void sendEmail(String recipient, String subject, String body) {
        // 1. Tangled rate limiting
        if (isRateLimited(recipient)) return;

        // 2. Tangled retry loop
        int attempts = 0;
        while (attempts < 3) {
            try {
                // 3. SMTP dispatch buried inside retry loop
                smtpClient.send(...);
                break;
            } catch (Exception e) {
                Thread.sleep(100);
            }
        }
        // 4. Tangled timing & logging
    }
}
```

#### Why This Fails:
1. **Violation of the Single Responsibility Principle (SRP):** The service is responsible for SMTP protocol integration, rate limiting algorithms, retry policies, backoff sleeping, and logging format.
2. **Exponential Code Duplication:** Adding `sendSlack()` and `sendSms()` requires copy-pasting the exact same 40-line retry loop, sleep logic, and rate limiting timestamps for every channel.
3. **Violation of the Open/Closed Principle (OCP):** Adding a new resilience policy (e.g., Circuit Breaker or Fallback Channel) requires editing every single notification method.
4. **Untestable in Isolation:** You cannot test whether the SMTP client formats headers properly without triggering sleep timers and rate-limit drops.

---

## 3. Improved Design (Decorator Pattern)

The improved design in `good/` decomposes operational concerns into **pluggable interceptor decorators** that wrap any `Notifier`:

```
new MetricsNotifierDecorator(
    new LoggingNotifierDecorator(
        new RateLimitingNotifierDecorator(
            new RetryNotifierDecorator(
                new EmailNotifier(),
                3,
                100
            ),
            1000
        )
    )
)
```

### Participants
- **`Notifier` (Component Abstraction):** Declares `void send(String recipient, String subject, String message)`.
- **`EmailNotifier`, `SlackNotifier`, `SmsNotifier` (Concrete Components):** Handle strictly channel-specific dispatch logic.
- **`NotifierDecorator` (Abstract Decorator):** Implements `Notifier` and aggregates a `protected final Notifier wrappee`.
- **`LoggingNotifierDecorator` (Concrete Decorator):** Measures elapsed time and logs delivery status.
- **`RetryNotifierDecorator` (Concrete Decorator):** Traps transient exceptions and retries with backoff.
- **`RateLimitingNotifierDecorator` (Concrete Decorator):** Intercepts rapid requests and drops excess traffic.
- **`MetricsNotifierDecorator` (Concrete Decorator):** Increments atomic telemetry counters for operations dashboards.

---

## 4. Execution Flow: Interception and Recovery

When a message is sent to a failing gateway:

```
Client Invocation: pipeline.send(recipient, subject, msg)
  │
  ▼
[MetricsNotifierDecorator]
  Increment total invocations counter
  │
  ▼
[LoggingNotifierDecorator]
  Log: "Starting dispatch to recipient..."
  Start stopwatch
  │
  ▼
[RateLimitingNotifierDecorator]
  Check timestamp: within threshold? YES -> proceed
  │
  ▼
[RetryNotifierDecorator]
  Attempt 1:
    │
    ▼
  [EmailNotifier] ---> throws "SMTP Connection Timeout"
    │
  Retry caught exception!
  Sleep 100ms backoff...
  Attempt 2:
    │
    ▼
  [EmailNotifier] ---> Dispatches successfully!
  │
  ▼
[LoggingNotifierDecorator]
  Stopwatch stopped (166 ms). Log: "Successfully delivered."
  │
  ▼
[MetricsNotifierDecorator]
  Increment successful requests counter
```

Notice that the client and outer decorators are **completely shielded from the transient error**. The retry decorator self-healed the failure.

---

## 5. Architectural Comparison: Decorator vs. Alternatives

### Decorator vs. Chain of Responsibility
- In **Decorator**, the wrapper usually delegates down to the core component and passes the result back up. In this example, `RateLimitingNotifierDecorator` conditionally short-circuits. While this mimics a filter, the primary intent remains adding operational capabilities to an existing component.
- In **Chain of Responsibility**, independent handler nodes decide whether to handle a command or pass it to a successor; there is no core "leaf component" being wrapped.

### Decorator vs. Spring AOP / Dynamic Proxies
In enterprise frameworks (e.g., Spring `@Retryable`, `@Timed`), decorators are often generated dynamically at runtime via byte-code weaving or JDK Dynamic Proxies. The GoF Decorator pattern is the fundamental design pattern underpinning these aspect-oriented mechanisms.

---

## 6. Relationship to SOLID Principles

- **Single Responsibility Principle (SRP):** `RetryNotifierDecorator` does only retries. `EmailNotifier` does only email formatting.
- **Open/Closed Principle (OCP):** Adding a `TelegramNotifier` or a `CircuitBreakerDecorator` requires writing one class with zero edits to existing classes.
- **Liskov Substitution Principle (LSP):** A `MetricsNotifierDecorator(SlackNotifier)` can be injected anywhere a raw `Notifier` is expected.
- **Dependency Inversion Principle (DIP):** Both core dispatchers and operational wrappers depend solely on the `Notifier` interface.

---

## 7. How to Identify Incorrect Implementations

- **Swallowing Exceptions Silently in Retry:** If a retry decorator exhausts all attempts and catches the final exception without rethrowing, the client will falsely believe the notification succeeded.
- **State Leak Across Non-Thread-Safe Decorators:** A rate limiter using a regular `HashMap` instead of `ConcurrentHashMap` will cause race conditions and data corruption in multi-threaded application servers.
- **Order Inversion:** Placing `MetricsNotifierDecorator` inside `RetryNotifierDecorator` will cause the metrics decorator to count each individual retry attempt as a separate client request rather than tracking overall business transaction outcomes.
