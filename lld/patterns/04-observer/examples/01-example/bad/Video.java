package bad;

/**
 * Domain representation of a video upload.
 */
public record Video(String videoId, String title, int durationMinutes) {
    public Video {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Video title cannot be blank.");
        }
    }
}
