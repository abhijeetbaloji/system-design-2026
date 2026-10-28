package good;

/**
 * Domain representation of a video upload.
 * Immutable record carrying event payload data.
 */
public record Video(String videoId, String title, int durationMinutes) {
    public Video {
        if (videoId == null || videoId.isBlank()) {
            throw new IllegalArgumentException("Video ID cannot be blank.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Video title cannot be blank.");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Video duration must be positive.");
        }
    }
}
