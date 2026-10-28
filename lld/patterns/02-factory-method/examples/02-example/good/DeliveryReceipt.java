package good;

/**
 * Domain record returned after transport dispatch completion.
 */
public record DeliveryReceipt(
        String trackingId,
        String transportMode,
        String vehicleIdentifier,
        double freightCost,
        String confirmationStatus
) {}
