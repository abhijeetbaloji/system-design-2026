package bad;

/**
 * Concrete email notification service with its own unique API method.
 */
public class EmailAlertService {
    private final String mailingListAddress;

    public EmailAlertService(String mailingListAddress) {
        this.mailingListAddress = mailingListAddress;
    }

    public void dispatchEmailDigest(String channelName, Video video) {
        System.out.printf("  [Email Service] Dispatched email digest to <%s> for '%s' on %s%n",
                mailingListAddress, video.title(), channelName);
    }
}
