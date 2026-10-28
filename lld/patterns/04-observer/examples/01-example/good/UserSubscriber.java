package good;

/**
 * Concrete Observer: Represents an individual human viewer on YouTube.
 * Receives in-app notification bell alerts when subscribed channels upload.
 */
public class UserSubscriber implements Subscriber {
    private final String username;
    private int notificationCount = 0;

    public UserSubscriber(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        this.username = username;
    }

    @Override
    public void onNewVideoUploaded(String channelName, Video video) {
        notificationCount++;
        System.out.printf("  [User Bell Notification] Hey %s! '%s' just uploaded '%s' (%d mins)!%n",
                username, channelName, video.title(), video.durationMinutes());
    }

    @Override
    public String getSubscriberId() {
        return username;
    }

    public int getNotificationCount() {
        return notificationCount;
    }
}
