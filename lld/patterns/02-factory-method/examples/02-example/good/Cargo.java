package good;

/**
 * Domain record representing shipment cargo.
 */
public record Cargo(String trackingId, String description, double weightKg) {
    public Cargo {
        if (trackingId == null || trackingId.isBlank()) {
            throw new IllegalArgumentException("Tracking ID must not be blank.");
        }
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Cargo weight must be strictly positive.");
        }
    }
}
