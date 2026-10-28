package good;

/**
 * Concrete Observer: Represents an email digest notification system.
 * Formats and dispatches email notifications to user inboxes.
 */
public class EmailSubscriber implements Subscriber {
    private final String emailAddress;
    private int emailsSent = 0;

    public EmailSubscriber(String emailAddress) {
        if (emailAddress == null || emailAddress.isBlank()) {
            throw new IllegalArgumentException("Email address cannot be blank.");
        }
        this.emailAddress = emailAddress;
    }

    @Override
    public void onNewVideoUploaded(String channelName, Video video) {
        emailsSent++;
        System.out.printf("  [Email Service] Dispatched email to <%s>: Channel '%s' uploaded '%s' (Watch: https://youtu.be/%s)%n",
                emailAddress, channelName, video.title(), video.videoId());
    }

    @Override
    public String getSubscriberId() {
        return "EMAIL:" + emailAddress;
    }

    public int getEmailsSent() {
        return emailsSent;
    }
}
