package bad;

public class BadStockDemo {
    public static void main(String[] args) {
        System.out.println("=== Bad Stock Feed Notification Demo ===");

        BadStockFeed feed = new BadStockFeed();

        feed.publishTick(new StockTick("NVDA", 125.50, +2.3));

        // User closes the ticker widget in UI
        System.out.println("\n--- User Closes Ticker Display Window ---");
        feed.getTickerDisplay().closeWidget();

        // New tick arrives: feed still calls the closed widget (Lapsed Listener leak)
        feed.publishTick(new StockTick("NVDA", 205.80, +64.0));

        System.out.println("\nProblems Exposed:");
        System.out.println("1. Lapsed listener: BadStockFeed continues holding reference and updating closed display.");
        System.out.println("2. Adding charting panels or algorithmic bots requires modifying BadStockFeed.java.");
        System.out.println("3. No pull mechanism for observers wanting extra statistical data.");
    }
}
