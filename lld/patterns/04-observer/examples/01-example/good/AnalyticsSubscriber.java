package good;

/**
 * Concrete Observer: Represents Creator Studio Analytics.
 * Ingests upload events to update dashboard tracking and view projection metrics.
 */
public class AnalyticsSubscriber implements Subscriber {
    private final String analyticsEngineId;
    private int trackedUploads = 0;

    public AnalyticsSubscriber(String analyticsEngineId) {
        this.analyticsEngineId = analyticsEngineId;
    }

    @Override
    public void onNewVideoUploaded(String channelName, Video video) {
        trackedUploads++;
        System.out.printf("  [Creator Studio Analytics] System '%s' logged telemetry for '%s' [%s]. Initialized real-time counters.%n",
                analyticsEngineId, video.title(), video.videoId());
    }

    @Override
    public String getSubscriberId() {
        return "ANALYTICS:" + analyticsEngineId;
    }

    public int getTrackedUploads() {
        return trackedUploads;
    }
}
