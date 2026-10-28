package good;

/**
 * Subject (Publisher) Interface.
 *
 * Defines methods for attaching, detaching, and notifying subscriber observers.
 */
public interface Channel {

    /**
     * Attaches an observer to the subscriber registry.
     * Must be idempotent (registering the same observer twice should not duplicate).
     */
    void subscribe(Subscriber subscriber);

    /**
     * Detaches an observer from the subscriber registry.
     */
    void unsubscribe(Subscriber subscriber);

    /**
     * Broadcasts an upload notification to all currently subscribed observers.
     */
    void notifySubscribers(Video video);

    /**
     * Returns the current count of active subscribers.
     */
    int getSubscriberCount();
}
