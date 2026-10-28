package good;

/**
 * Product Interface.
 *
 * Declares the standard capabilities expected of any freight transport carrier.
 * The Creator (Logistics) depends solely on this contract.
 */
public interface Transport {

    /**
     * Executes the delivery operation for the given cargo and route distance.
     *
     * @param cargo      The cargo shipment details.
     * @param distanceKm The transit distance in kilometers.
     * @return A verified DeliveryReceipt containing dispatch metadata.
     */
    DeliveryReceipt deliver(Cargo cargo, double distanceKm);

    /**
     * Checks if this transport vehicle can accommodate the cargo weight.
     *
     * @param cargo The cargo to validate.
     * @return true if within payload limits, false otherwise.
     */
    boolean canHandle(Cargo cargo);

    /**
     * Calculates the estimated freight cost based on transport-specific pricing models.
     */
    double calculateCost(Cargo cargo, double distanceKm);

    /**
     * Returns the human-readable transport mode (e.g., "ROAD", "SEA").
     */
    String getTransportMode();
}
