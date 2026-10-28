package good;

/**
 * Automated test suite for YouTubeChannel Observer implementation.
 * Validates correctness of subscription lifecycle, notification delivery,
 * concurrency safety, and error isolation.
 */
public class YouTubeChannelTest {

    public static void main(String[] args) {
        System.out.println("Running YouTubeChannelTest...");

        testSubscriptionAndNotification();
        testUnsubscriptionStopsNotification();
        testDuplicateSubscriptionPrevented();
        testSafeUnsubscriptionDuringNotification();
        testFaultIsolationWhenSubscriberThrows();

        System.out.println("ALL 5 TESTS PASSED SUCCESSFULLY!\n");
    }

    private static void testSubscriptionAndNotification() {
        YouTubeChannel channel = new YouTubeChannel("TestChannel");
        UserSubscriber user = new UserSubscriber("Alice");
        EmailSubscriber email = new EmailSubscriber("alice@test.com");

        channel.subscribe(user);
        channel.subscribe(email);

        assertEquals(2, channel.getSubscriberCount(), "Subscriber count should be 2");

        channel.uploadVideo(new Video("V1", "Java 21 Virtual Threads", 15));

        assertEquals(1, user.getNotificationCount(), "Alice should receive 1 notification");
        assertEquals(1, email.getEmailsSent(), "Email service should dispatch 1 email");
        System.out.println("  ✓ testSubscriptionAndNotification passed.");
    }

    private static void testUnsubscriptionStopsNotification() {
        YouTubeChannel channel = new YouTubeChannel("TestChannel");
        UserSubscriber bob = new UserSubscriber("Bob");

        channel.subscribe(bob);
        channel.uploadVideo(new Video("V1", "Part 1", 10));
        assertEquals(1, bob.getNotificationCount(), "Bob should receive first notification");

        channel.unsubscribe(bob);
        assertEquals(0, channel.getSubscriberCount(), "Subscriber count should be 0");

        channel.uploadVideo(new Video("V2", "Part 2", 12));
        assertEquals(1, bob.getNotificationCount(), "Bob should NOT receive notification after unsubscribing");
        System.out.println("  ✓ testUnsubscriptionStopsNotification passed.");
    }

    private static void testDuplicateSubscriptionPrevented() {
        YouTubeChannel channel = new YouTubeChannel("TestChannel");
        UserSubscriber user = new UserSubscriber("Charlie");

        channel.subscribe(user);
        channel.subscribe(user); // Duplicate attempt

        assertEquals(1, channel.getSubscriberCount(), "Duplicate subscriber should not increase count");

        channel.uploadVideo(new Video("V1", "Algorithms", 20));
        assertEquals(1, user.getNotificationCount(), "Charlie should receive exactly 1 notification, not 2");
        System.out.println("  ✓ testDuplicateSubscriptionPrevented passed.");
    }

    private static void testSafeUnsubscriptionDuringNotification() {
        YouTubeChannel channel = new YouTubeChannel("TestChannel");

        // A one-time subscriber that unregisters itself upon receiving its first notification
        Subscriber selfUnsubscribingUser = new Subscriber() {
            @Override
            public void onNewVideoUploaded(String channelName, Video video) {
                channel.unsubscribe(this); // Modifies observer list during active notification!
            }

            @Override
            public String getSubscriberId() {
                return "SelfUnsubscribingUser";
            }
        };

        UserSubscriber regularUser = new UserSubscriber("Dave");

        channel.subscribe(selfUnsubscribingUser);
        channel.subscribe(regularUser);

        // This must not throw ConcurrentModificationException
        channel.uploadVideo(new Video("V1", "Concurrency In Practice", 30));

        assertEquals(1, regularUser.getNotificationCount(), "Dave should still be notified");
        assertEquals(1, channel.getSubscriberCount(), "Only Dave should remain subscribed");
        System.out.println("  ✓ testSafeUnsubscriptionDuringNotification passed.");
    }

    private static void testFaultIsolationWhenSubscriberThrows() {
        YouTubeChannel channel = new YouTubeChannel("TestChannel");

        Subscriber buggySubscriber = new Subscriber() {
            @Override
            public void onNewVideoUploaded(String channelName, Video video) {
                throw new RuntimeException("Simulated unexpected network crash in buggy observer!");
            }

            @Override
            public String getSubscriberId() {
                return "BuggySubscriber";
            }
        };

        UserSubscriber safeUser = new UserSubscriber("Emma");

        channel.subscribe(buggySubscriber);
        channel.subscribe(safeUser);

        // When video is uploaded, buggySubscriber will fail, but safeUser MUST still be notified
        channel.uploadVideo(new Video("V1", "Resilience Patterns", 25));

        assertEquals(1, safeUser.getNotificationCount(), "Emma must receive notification despite buggy observer exception");
        System.out.println("  ✓ testFaultIsolationWhenSubscriberThrows passed.");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(String.format("Assertion failed: %s (Expected %d, got %d)",
                    message, expected, actual));
        }
    }
}
