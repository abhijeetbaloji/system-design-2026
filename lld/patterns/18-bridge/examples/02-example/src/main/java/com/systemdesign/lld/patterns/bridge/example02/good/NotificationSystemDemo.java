package com.systemdesign.lld.patterns.bridge.example02.good;

public class NotificationSystemDemo {
    public static void main(String[] args) {
        EmailMessageChannel email = new EmailMessageChannel();
        SmsMessageChannel sms = new SmsMessageChannel();
        PushNotificationChannel push = new PushNotificationChannel();

        NotificationDispatchService service = new NotificationDispatchService();

        // 1. Normal notification via Email
        Notification normal = new NormalNotification(email);
        service.dispatch(normal, "alice@example.com", "Your monthly billing receipt is available.");

        // 2. Urgent notification: Primary is SMS, fallback is Email
        UrgentNotification urgent = new UrgentNotification(sms, email);
        System.out.println("--- Dispatching urgent alert while SMS is online ---");
        service.dispatch(urgent, "+1-555-0199", "Suspicious payment attempt detected.");

        // Simulate SMS gateway outage
        sms.setAvailable(false);
        System.out.println("--- Dispatching urgent alert during SMS outage (automatic failover) ---");
        service.dispatch(urgent, "alice@example.com", "Second verification code: 849201");

        // 3. Critical alert redundant broadcast across Push and SMS
        sms.setAvailable(true);
        CriticalAlertNotification critical = new CriticalAlertNotification(push, sms);
        System.out.println("--- Dispatching critical broadcast ---");
        service.dispatch(critical, "devops-lead", "Production database cluster node failure!");

        System.out.println("\n=== EMAIL SENT LOG ===");
        email.getSentLog().forEach(System.out::println);

        System.out.println("\n=== SMS SENT LOG ===");
        sms.getSentLog().forEach(System.out::println);

        System.out.println("\n=== PUSH SENT LOG ===");
        push.getSentLog().forEach(System.out::println);
    }
}
