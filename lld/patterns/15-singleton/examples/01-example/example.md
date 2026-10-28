# Shared Application Configuration Singleton Example

## 1. Scenario Overview

In any enterprise software system (such as an e-commerce platform or financial exchange), runtime configuration governs critical operational parameters:
- `db.pool.max_connections`: Maximum active database connections.
- `payment.gateway.url`: Endpoint for payment processing.
- `order.rate_limit.per_second`: Rate limiting thresholds.
- `feature_flag.new_checkout`: Live feature flag toggles.

These configuration values must be consistent across all worker threads and services. If `OrderService` and `PaymentService` maintain separate configuration instances, updates made dynamically by an ops engineer to the feature flag or rate limit will only be seen by one component, creating split-brain bugs.

Using the **Singleton Pattern with Bill Pugh Static Inner Holder**, the application guarantees:
1. Exactly one configuration instance in memory.
2. Lazy, thread-safe initialization without costly synchronization locks.
3. Defense against reflection and serialization attacks.
4. Adherence to Dependency Inversion (clients depend on `ConfigurationService` interface for testability).

---

## 2. Bad Design vs Good Design

### Bad Design (Multiple Uncoordinated Instances)
- `BadConfigurationManager` has a public constructor.
- Each service creates its own instance (`new BadConfigurationManager()`).
- When an operator changes a setting in one service, the other service retains stale settings.
- File or database I/O is re-executed on every instantiation.

### Good Design (Bill Pugh Singleton with Defense Mechanisms)
- `AppConfigSingleton` has a `private` constructor that throws an exception if invoked reflectively when an instance already exists.
- The Bill Pugh static inner `Holder` ensures lazy, thread-safe loading on first call to `getInstance()`.
- Implements `readResolve()` to ensure serialization returns the same singleton instance.
- Implements `ConfigurationService` so services (`OrderService`, `PaymentService`) can be injected and mocked in unit tests.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.singleton.example01
├── bad
│   ├── BadConfigDemo.java
│   └── BadConfigurationManager.java
└── good
    ├── AppConfigSingleton.java
    ├── ConfigDemo.java
    ├── ConfigurationService.java
    ├── OrderService.java
    └── PaymentService.java
```

---

## 4. Execution Flow & SOLID Principles

- **Bill Pugh Lazy Loading**: Calling `AppConfigSingleton.getInstance()` causes the JVM class loader to load `Holder.INSTANCE`, initializing default properties into a `ConcurrentHashMap` once.
- **Single Responsibility Principle (SRP)**: `AppConfigSingleton` manages application configuration; business services manage their own domains.
- **Dependency Inversion Principle (DIP)**: `OrderService` depends on the `ConfigurationService` interface, allowing test doubles to be injected without touching the singleton.
