# Service & Plugin Registry Enum Singleton Example

## 1. Scenario Overview

Modern modular architectures (e.g., OSGi, Spring ApplicationContext, Microkernel architectures) require a centralized **Service Registry / Service Locator**:
- Plugin modules register implementations (e.g., `PaymentGateway`, `NotificationGateway`, `StorageDriver`).
- Business controllers resolve those implementations dynamically at runtime by type or by registered name.
- If multiple disparate registries exist in memory, an extension registered by the payment module in Registry A cannot be discovered by the checkout workflow looking inside Registry B.

Using the **Enum Singleton Pattern** (as championed by Joshua Bloch in *Effective Java*), `ServiceRegistrySingleton` provides:
1. **Absolute Uniqueness**: The Java Virtual Machine guarantees that exactly one enum constant exists per JVM.
2. **Native Reflection Immunity**: The JVM forbids reflection instantiation of enum types (`Constructor.newInstance()` explicitly throws `IllegalArgumentException: Cannot reflectively create enum objects`).
3. **Automatic Serialization Safety**: Enums are serialized based purely on their name; deserialization does not create duplicate instances.
4. **Thread Safety**: Backed by `ConcurrentHashMap` for concurrent registration and lookup.

---

## 2. Bad Design vs Good Design

### Bad Design (Multiple Independent Registries)
- Modules instantiate distinct `BadServiceRegistry` instances.
- Registering a service in the bootstrap module is invisible to worker controllers that hold their own registry instance.
- Service lookup fails unexpectedly at runtime with `NullPointerException` or "Service Not Found".

### Good Design (Enum Singleton Pattern)
- `ServiceRegistrySingleton.INSTANCE` is the process-wide single source of truth for service discovery.
- Implements `ServiceRegistry` interface to preserve testability and dependency injection.
- Guarantees thread-safe registration and resolution across all application modules.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.singleton.example03
├── bad
│   ├── BadRegistryDemo.java
│   └── BadServiceRegistry.java
└── good
    ├── CheckoutController.java
    ├── NotificationGateway.java
    ├── PaymentGateway.java
    ├── RegistryDemo.java
    ├── SendGridNotification.java
    ├── ServiceRegistry.java
    ├── ServiceRegistrySingleton.java
    └── StripeGateway.java
```

---

## 4. Execution Flow & SOLID Principles

- **Type-Safe Registration & Discovery**:
  - `ServiceRegistrySingleton.INSTANCE.register(PaymentGateway.class, new StripeGateway())`
  - `PaymentGateway gateway = registry.resolve(PaymentGateway.class).orElseThrow(...)`
- **Dependency Inversion Principle (DIP)**: `CheckoutController` depends on the `ServiceRegistry` abstraction rather than hardcoded service implementations.
- **Single Responsibility Principle (SRP)**: The registry manages service mappings; domain services execute business logic.
