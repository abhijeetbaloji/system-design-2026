package bad;

/**
 * BAD DESIGN: Monolithic YouTube channel tightly coupled to specific subscribers.
 *
 * Flaws exposed:
 * 1. Violates Open/Closed Principle (OCP): Adding a new subscriber (e.g. mobile push, analytics)
 *    requires editing BadYouTubeChannel and adding new fields and method calls.
 * 2. Violates Dependency Inversion Principle (DIP): High-level channel depends on low-level concrete classes.
 * 3. Static and rigid: Cannot dynamically subscribe or unsubscribe viewers at runtime.
 * 4. Inconsistent interfaces: Direct calls to different method signatures (ringBell, dispatchEmailDigest).
 */
public class BadYouTubeChannel {
    private final String channelName;

    // Hardcoded concrete listener references
    private final User alice = new User("Alice");
    private final User bob = new User("Bob");
    private final EmailAlertService emailService = new EmailAlertService("subscribers-digest@techbites.com");

    public BadYouTubeChannel(String channelName) {
        this.channelName = channelName;
    }

    public void uploadVideo(Video video) {
        System.out.printf("[%s] Uploaded new video: '%s'%n", channelName, video.title());

        // Hardcoded, tightly coupled notification dispatching
        alice.ringBell(video);
        bob.ringBell(video);
        emailService.dispatchEmailDigest(channelName, video);

        System.out.println("  [Audit] Notifications dispatched manually.\n");
    }
}
