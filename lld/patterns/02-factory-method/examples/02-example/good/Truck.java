package good;

/**
 * Concrete Product: Truck (Road Transport).
 *
 * Implements road transport mechanics, highway freight calculation,
 * and payload constraints.
 */
public class Truck implements Transport {
    private final String licensePlate;
    private final double maxPayloadKg;

    public Truck(String licensePlate, double maxPayloadKg) {
        this.licensePlate = licensePlate;
        this.maxPayloadKg = maxPayloadKg;
    }

    @Override
    public boolean canHandle(Cargo cargo) {
        return cargo.weightKg() <= maxPayloadKg;
    }

    @Override
    public double calculateCost(Cargo cargo, double distanceKm) {
        double ratePerKm = 1.85;
        double highwayTollFee = 45.00;
        return (distanceKm * ratePerKm) + highwayTollFee;
    }

    @Override
    public DeliveryReceipt deliver(Cargo cargo, double distanceKm) {
        if (!canHandle(cargo)) {
            throw new IllegalStateException(String.format(
                    "Cargo [%.1f kg] exceeds truck payload capacity of %.1f kg.",
                    cargo.weightKg(), maxPayloadKg));
        }

        double cost = calculateCost(cargo, distanceKm);
        System.out.printf("  [Road Hauler] Truck '%s' dispatched via highway.%n", licensePlate);
        System.out.printf("  [Road Hauler] Transporting %s (%.1f kg) over %.1f km.%n",
                cargo.description(), cargo.weightKg(), distanceKm);

        return new DeliveryReceipt(
                cargo.trackingId(),
                getTransportMode(),
                licensePlate,
                cost,
                "DISPATCHED_ON_HIGHWAY"
        );
    }

    @Override
    public String getTransportMode() {
        return "ROAD";
    }
}
