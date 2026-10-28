package good;

/**
 * Concrete Creator: RoadLogistics.
 *
 * Configures fleet trucks for road highway freight delivery.
 */
public class RoadLogistics extends Logistics {
    private final String licensePlate;
    private final double maxPayloadKg;

    public RoadLogistics() {
        this("VOLVO-FH16-884", 25000.0);
    }

    public RoadLogistics(String licensePlate, double maxPayloadKg) {
        this.licensePlate = licensePlate;
        this.maxPayloadKg = maxPayloadKg;
    }

    @Override
    protected Transport createTransport() {
        // Factory Method implementation: instantiates a Truck
        return new Truck(licensePlate, maxPayloadKg);
    }
}
