package good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Concrete Subject: YouTubeChannel.
 *
 * Maintains a dynamic collection of {@link Subscriber} observers and broadcasts
 * notifications when a new video is published.
 *
 * Edge cases handled:
 * 1. Concurrent modification safety: Uses CopyOnWriteArrayList so an observer can
 *    safely unsubscribe during notification delivery.
 * 2. Idempotent registration: Duplicate subscriptions are detected and prevented.
 * 3. Fault isolation: If an individual observer throws an exception, the loop
 *    continues so remaining subscribers still receive their notification.
 */
public class YouTubeChannel implements Channel {
    private final String channelName;
    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();
    private final List<Video> uploadedVideos = new ArrayList<>();

    public YouTubeChannel(String channelName) {
        if (channelName == null || channelName.isBlank()) {
            throw new IllegalArgumentException("Channel name cannot be blank.");
        }
        this.channelName = channelName;
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("Subscriber cannot be null.");
        }
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
            System.out.printf("[%s] Subscriber added: %s (Total subscribers: %d)%n",
                    channelName, subscriber.getSubscriberId(), subscribers.size());
        } else {
            System.out.printf("[%s] Subscriber %s is already subscribed. Ignored duplicate.%n",
                    channelName, subscriber.getSubscriberId());
        }
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        if (subscriber != null && subscribers.remove(subscriber)) {
            System.out.printf("[%s] Subscriber removed: %s (Total subscribers: %d)%n",
                    channelName, subscriber.getSubscriberId(), subscribers.size());
        }
    }

    @Override
    public void notifySubscribers(Video video) {
        System.out.printf("[%s] Broadcasting upload notification for '%s' to %d subscribers...%n",
                channelName, video.title(), subscribers.size());

        for (Subscriber subscriber : subscribers) {
            try {
                subscriber.onNewVideoUploaded(channelName, video);
            } catch (Exception ex) {
                // Fault isolation: one faulty subscriber does not break notification for others
                System.err.printf("  [WARN] Failed to notify subscriber %s: %s%n",
                        subscriber.getSubscriberId(), ex.getMessage());
            }
        }
    }

    /**
     * Business method: Uploads a video and automatically triggers notifications.
     */
    public void uploadVideo(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("Video cannot be null.");
        }
        uploadedVideos.add(video);
        System.out.printf("%n=== [%s] Uploaded New Video: '%s' (%d mins) ===%n",
                channelName, video.title(), video.durationMinutes());
        notifySubscribers(video);
    }

    @Override
    public int getSubscriberCount() {
        return subscribers.size();
    }

    public String getChannelName() {
        return channelName;
    }

    public List<Video> getUploadedVideos() {
        return Collections.unmodifiableList(uploadedVideos);
    }
}
