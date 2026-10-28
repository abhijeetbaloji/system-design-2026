package bad;

/**
 * Concrete user receiving direct mobile bell notifications.
 */
public class User {
    private final String username;

    public User(String username) {
        this.username = username;
    }

    public void ringBell(Video video) {
        System.out.printf("  [User Bell Notification] Hey %s! New video: '%s' (%d mins)%n",
                username, video.title(), video.durationMinutes());
    }

    public String getUsername() {
        return username;
    }
}
