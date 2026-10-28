package good;

/**
 * Concrete Product: Ship (Sea Transport).
 *
 * Implements ocean freight mechanics, maritime bill of lading,
 * and bulk container constraints.
 */
public class Ship implements Transport {
    private final String vesselName;
    private final String imoNumber;
    private final double maxPayloadKg;

    public Ship(String vesselName, String imoNumber, double maxPayloadKg) {
        this.vesselName = vesselName;
        this.imoNumber = imoNumber;
        this.maxPayloadKg = maxPayloadKg;
    }

    @Override
    public boolean canHandle(Cargo cargo) {
        return cargo.weightKg() <= maxPayloadKg;
    }

    @Override
    public double calculateCost(Cargo cargo, double distanceKm) {
        double oceanRatePerKm = 0.45;
        double harborDues = 1200.00;
        return (distanceKm * oceanRatePerKm) + harborDues;
    }

    @Override
    public DeliveryReceipt deliver(Cargo cargo, double distanceKm) {
        if (!canHandle(cargo)) {
            throw new IllegalStateException(String.format(
                    "Cargo [%.1f kg] exceeds bulk cargo vessel capacity of %.1f kg.",
                    cargo.weightKg(), maxPayloadKg));
        }

        double cost = calculateCost(cargo, distanceKm);
        System.out.printf("  [Maritime Cargo] Vessel '%s' (IMO: %s) cleared for open sea.%n", vesselName, imoNumber);
        System.out.printf("  [Maritime Cargo] Container loaded: %s (%.1f kg) across %.1f nautical km.%n",
                cargo.description(), cargo.weightKg(), distanceKm);

        return new DeliveryReceipt(
                cargo.trackingId(),
                getTransportMode(),
                vesselName + " (" + imoNumber + ")",
                cost,
                "LADEN_ON_BOARD_OCEAN_TRANSIT"
        );
    }

    @Override
    public String getTransportMode() {
        return "SEA";
    }
}
