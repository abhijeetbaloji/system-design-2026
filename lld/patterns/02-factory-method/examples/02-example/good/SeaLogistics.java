package good;

/**
 * Concrete Creator: SeaLogistics.
 *
 * Configures maritime container vessels for ocean freight shipping.
 */
public class SeaLogistics extends Logistics {
    private final String vesselName;
    private final String imoNumber;
    private final double maxPayloadKg;

    public SeaLogistics() {
        this("Atlantic Star", "IMO-9142851", 750000.0);
    }

    public SeaLogistics(String vesselName, String imoNumber, double maxPayloadKg) {
        this.vesselName = vesselName;
        this.imoNumber = imoNumber;
        this.maxPayloadKg = maxPayloadKg;
    }

    @Override
    protected Transport createTransport() {
        // Factory Method implementation: instantiates a Ship
        return new Ship(vesselName, imoNumber, maxPayloadKg);
    }
}
