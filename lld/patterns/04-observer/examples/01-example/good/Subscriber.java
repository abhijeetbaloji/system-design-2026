package good;

/**
 * Observer Interface.
 *
 * Defines the contract that all YouTube subscribers must implement to receive
 * video upload broadcasts from a Channel (Subject).
 */
public interface Subscriber {

    /**
     * Callback invoked by the Subject when a new video is published.
     *
     * @param channelName The name of the publishing YouTube channel.
     * @param video       The new video metadata payload.
     */
    void onNewVideoUploaded(String channelName, Video video);

    /**
     * Unique identifier for the subscriber (username, service ID, etc.).
     */
    String getSubscriberId();
}
