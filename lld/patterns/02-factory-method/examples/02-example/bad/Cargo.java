package bad;

/**
 * Domain record representing shipment cargo.
 */
public record Cargo(String trackingId, String description, double weightKg) {
    public Cargo {
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Cargo weight must be positive.");
        }
    }
}
