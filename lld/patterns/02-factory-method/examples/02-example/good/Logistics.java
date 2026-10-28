package good;

/**
 * Creator (Abstract Class).
 *
 * Implements the core logistics and shipment planning algorithm.
 * Defines the standard lifecycle for preparing, cost estimating, and dispatching cargo.
 *
 * It delegates vehicle instantiation to the Factory Method:
 *   {@link #createTransport()}
 */
public abstract class Logistics {

    /**
     * Core business workflow (Template Method).
     * Coordinates the delivery planning without knowing the exact transport carrier.
     */
    public DeliveryReceipt planDelivery(Cargo cargo, double distanceKm) {
        if (distanceKm <= 0) {
            throw new IllegalArgumentException("Distance must be greater than zero.");
        }

        System.out.printf("--- [Logistics Hub] Planning Shipment [%s: %s] ---%n",
                cargo.trackingId(), cargo.description());

        // Step 1: Create transport via Factory Method
        Transport transport = createTransport();

        // Step 2: Validate capacity constraint
        if (!transport.canHandle(cargo)) {
            System.err.printf("  [REJECTED] Transport '%s' cannot handle cargo weight of %.1f kg.%n%n",
                    transport.getTransportMode(), cargo.weightKg());
            return new DeliveryReceipt(
                    cargo.trackingId(),
                    transport.getTransportMode(),
                    "NONE",
                    0.0,
                    "REJECTED_EXCEEDS_CAPACITY"
            );
        }

        // Step 3: Compute cost estimate
        double estimatedCost = transport.calculateCost(cargo, distanceKm);
        System.out.printf("  [Quote] Estimated %s freight cost for %.1f km: $%.2f%n",
                transport.getTransportMode(), distanceKm, estimatedCost);

        // Step 4: Dispatch shipment
        DeliveryReceipt receipt = transport.deliver(cargo, distanceKm);

        // Step 5: Post-dispatch auditing
        recordShipmentLedger(receipt);

        return receipt;
    }

    private void recordShipmentLedger(DeliveryReceipt receipt) {
        System.out.printf("  [Central Ledger] Confirmed %s receipt for Tracking #%s: $%.2f [%s]%n%n",
                receipt.transportMode(), receipt.trackingId(), receipt.freightCost(), receipt.confirmationStatus());
    }

    /**
     * THE FACTORY METHOD.
     * Concrete subclasses override this to instantiate Road, Sea, or Air transports.
     */
    protected abstract Transport createTransport();
}
