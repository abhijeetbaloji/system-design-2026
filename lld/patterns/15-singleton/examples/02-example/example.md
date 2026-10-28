# Process-Wide Logger Singleton Example

## 1. Scenario Overview

In server-side architectures, multi-threaded processes generate high-velocity diagnostic and audit logs.
- A single process might run dozens of worker threads processing requests concurrently (e.g., authentication, checkout, database pooling).
- All threads must pipe their log statements (`DEBUG`, `INFO`, `WARN`, `ERROR`) to a central logging subsystem.
- Without central coordination, multiple loggers opening simultaneous file handles to `/var/log/app.log` trigger OS file lock conflicts, corrupted line interspersal, and high resource leaks.

A **Double-Checked Locking (DCL) Singleton** with `volatile` (`ProcessLoggerSingleton`):
1. Ensures all worker threads and services write to the exact same thread-safe in-memory buffer or output stream.
2. Synchronizes log writes without acquiring locks on `getInstance()` reads once initialized.
3. Provides centralized log-level filtering (`DEBUG`, `INFO`, `WARN`, `ERROR`).
4. Allows programmatic retrieval and clearing of logs for test isolation and audit export.

---

## 2. Bad Design vs Good Design

### Bad Design (Multiple Logger Instances)
- Each service instantiates its own `BadFileLogger`.
- Duplicate instances hold separate memory buffers and file writers.
- Changing log levels (e.g., enabling `DEBUG` mode during an incident) must be done on every logger instance independently.
- Log statements cannot be aggregated in chronological order across services.

### Good Design (Double-Checked Locking Singleton)
- `ProcessLoggerSingleton` is lazily instantiated via Double-Checked Locking with a `volatile` reference.
- Reflection defense in the constructor prevents unauthorized duplicate instantiations.
- Internal buffer uses thread-safe concurrent storage to handle multi-threaded log submissions without corrupting records.
- Implements `LoggerService` interface so services can be injected cleanly according to DIP.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.singleton.example02
├── bad
│   ├── BadFileLogger.java
│   └── BadLoggerDemo.java
└── good
    ├── AuthService.java
    ├── LogLevel.java
    ├── LogRecord.java
    ├── LoggerDemo.java
    ├── LoggerService.java
    ├── PaymentService.java
    └── ProcessLoggerSingleton.java
```

---

## 4. Execution Flow & SOLID Principles

- **Double-Checked Locking**: Thread A checks `instance == null`, synchronizes on `ProcessLoggerSingleton.class`, checks `instance == null` again, constructs the singleton, and assigns it to the `volatile instance`. Thread B checks `instance != null` and returns immediately without lock contention.
- **Single Responsibility Principle (SRP)**: `ProcessLoggerSingleton` focuses solely on log formatting, filtering, and buffering.
- **Dependency Inversion Principle (DIP)**: `AuthService` and `PaymentService` depend on the `LoggerService` interface, enabling easy mocking during tests.
