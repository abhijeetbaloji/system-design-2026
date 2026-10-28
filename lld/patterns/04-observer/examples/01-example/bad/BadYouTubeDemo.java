package bad;

public class BadYouTubeDemo {
    public static void main(String[] args) {
        System.out.println("=== Bad YouTube Notification Design Demo ===");

        BadYouTubeChannel channel = new BadYouTubeChannel("TechBites Official");

        Video video1 = new Video("VID-101", "Design Patterns: Observer Explained", 14);
        channel.uploadVideo(video1);

        System.out.println("Problems Exposed:");
        System.out.println("1. Alice and Bob are hardcoded inside BadYouTubeChannel.");
        System.out.println("2. Charlie cannot subscribe at runtime without editing channel source code.");
        System.out.println("3. Bob cannot unsubscribe when he no longer wants notifications.");
        System.out.println("4. Every new alert system requires modifying BadYouTubeChannel.java.");
    }
}
