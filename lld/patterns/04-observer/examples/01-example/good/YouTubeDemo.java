package good;

/**
 * Client Demonstration.
 *
 * Demonstrates:
 * 1. Attaching multiple diverse observers (Users, Email service, Analytics engine).
 * 2. Uploading a video and observing automatic polymorphic broadcast.
 * 3. Dynamic unsubscription (Bob unregisters).
 * 4. Subsequent upload reaching only active subscribers.
 */
public class YouTubeDemo {

    public static void main(String[] args) {
        System.out.println("=== Factory & Observer Patterns: YouTube Subscriptions Demo ===\n");

        YouTubeChannel channel = new YouTubeChannel("TechWithTim");

        // Observers
        Subscriber alice = new UserSubscriber("Alice");
        Subscriber bob = new UserSubscriber("Bob");
        Subscriber emailService = new EmailSubscriber("subscribers-weekly@techwithtim.com");
        Subscriber analytics = new AnalyticsSubscriber("STUDIO-METRICS-01");

        // 1. Dynamic subscriptions
        channel.subscribe(alice);
        channel.subscribe(bob);
        channel.subscribe(emailService);
        channel.subscribe(analytics);

        // Attempt duplicate subscription
        channel.subscribe(alice);

        // 2. First upload: all 4 observers are notified
        Video video1 = new Video("VID-201", "System Design: The Observer Pattern", 18);
        channel.uploadVideo(video1);

        // 3. Dynamic unsubscription: Bob decides to unsubscribe
        System.out.println("\n--- Bob Unsubscribes from Channel ---");
        channel.unsubscribe(bob);

        // 4. Second upload: Bob does not receive this notification
        Video video2 = new Video("VID-202", "Deep Dive: Concurrency and Thread Safety", 22);
        channel.uploadVideo(video2);

        System.out.printf("%nExecution Summary: Channel '%s' has %d active subscribers.%n",
                channel.getChannelName(), channel.getSubscriberCount());
    }
}
