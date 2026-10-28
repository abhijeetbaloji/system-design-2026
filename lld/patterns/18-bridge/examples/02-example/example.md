# Example 02: Notification System and Message Channels

## 1. Problem Scenario
An enterprise alerting platform must dispatch messages of varying operational urgency (`NormalNotification`, `UrgentNotification`, `CriticalAlertNotification`) across different communication delivery channels (`EmailMessageChannel`, `SmsMessageChannel`, `PushNotificationChannel`).

Without the Bridge pattern:
- Inheritance ties notification urgency to specific transport channels: `NormalEmailNotification`, `UrgentEmailNotification`, `CriticalEmailNotification`, `NormalSmsNotification`, `UrgentSmsNotification`, `CriticalSmsNotification`, etc.
- With $M$ urgency levels and $N$ channels, the system suffers an $M \times N$ class explosion.
- Features like dynamic fallback routing (e.g., if SMS fails or is unavailable, immediately retry via Email or Push) are virtually impossible without re-instantiating completely different class hierarchies.

## 2. Bad Design (Subclass Matrix)
- The bad design subclasses every urgency level for every transport channel.
- If a new communication channel (e.g., `SlackChannel`) is integrated, 3 new classes must be written.
- If a new urgency level (`ComplianceDigestNotification`) is added, 3 new classes must be written.
- Channel-specific concerns (payload size limits, SMTP headers) leak into high-level notification classes.

## 3. Good Design (Bridge Pattern)
- Decouples the **Abstraction** (`Notification`, `NormalNotification`, `UrgentNotification`, `CriticalAlertNotification`) from the **Implementor** (`MessageChannel`, `EmailMessageChannel`, `SmsMessageChannel`, `PushNotificationChannel`).
- The base `Notification` maintains a bridge reference `#MessageChannel primaryChannel` and an optional `#MessageChannel fallbackChannel`.
- High-level business logic is cleanly differentiated:
  - `NormalNotification`: Formats standard priority header and sends via primary channel.
  - `UrgentNotification`: Appends `[URGENT]`, and if the primary channel is unavailable or fails, automatically fails over to `fallbackChannel`.
  - `CriticalAlertNotification`: Appends siren alerts, logs incident audits, and broadcasts across *both* primary and fallback channels for guaranteed delivery.
- Adding a channel requires 1 new class. Adding an urgency level requires 1 new class. ($M + N$ scaling).

## 4. Class Diagram
See [diagram.mmd](file:///Users/abhijeet/Desktop/system-design-2026/lld/patterns/18-bridge/examples/02-example/diagram.mmd).

## 5. Execution Flow
1. Client initializes concrete channels (`EmailMessageChannel`, `SmsMessageChannel`, `PushNotificationChannel`).
2. Client creates an `UrgentNotification`, injecting `smsChannel` as primary and `emailChannel` as fallback.
3. Client invokes `notification.send("user@example.com", "Server CPU exceeds 95%")`.
4. If `smsChannel` is active, it dispatches.
5. If `smsChannel` goes offline (`smsChannel.setAvailable(false)`), `UrgentNotification` catches the failure and smoothly routes the alert through `emailChannel`.
