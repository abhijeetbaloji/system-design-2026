package bad;

public class BadNotificationDemo {
    public static void main(String[] args) {
        System.out.println("=== Bad Notification Design Demo ===");

        BadNotificationService service = new BadNotificationService();

        // Send first email (encounters retry on attempt 1, succeeds on attempt 2)
        System.out.println("\n--- Triggering Email 1 ---");
        service.sendEmail("alice@example.com", "Order Confirmation", "Your order #101 is confirmed!");

        // Send second email immediately to same recipient (triggers tangled rate-limit rejection)
        System.out.println("\n--- Triggering Rapid Email 2 (triggers rate limiter) ---");
        service.sendEmail("alice@example.com", "Order Update", "Your order has shipped!");

        System.out.println("\nFlaws identified:");
        System.out.println("1. Monolithic service tangles retry loops, sleep backoff, rate limits, and SMTP logic.");
        System.out.println("2. Adding Slack or SMS requires copy-pasting the same 40-line retry & rate limit code.");
        System.out.println("3. Impossible to disable retries in testing environments without altering production code.");
    }
}
