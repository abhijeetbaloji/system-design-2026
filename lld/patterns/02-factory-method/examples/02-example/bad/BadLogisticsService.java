package bad;

/**
 * BAD DESIGN: Monolithic logistics coordinator.
 *
 * Flaws exposed:
 * 1. Hardcodes truck and ship creation logic inside conditional statements.
 * 2. Blends route planning, toll calculations, maritime port fees, and vehicle instantiation.
 * 3. Violates OCP: Adding Air transport or Rail transport forces modifying this file.
 * 4. High coupling: Depends directly on concrete transport logic and details.
 */
public class BadLogisticsService {

    public void planAndDispatch(String transportMode, Cargo cargo, double distanceKm) {
        System.out.printf("--- Planning Delivery for Cargo [%s: %s (%.1f kg)] ---%n",
                cargo.trackingId(), cargo.description(), cargo.weightKg());

        if (transportMode.equalsIgnoreCase("ROAD")) {
            // Road Transport logic hardcoded
            if (cargo.weightKg() > 25000) {
                System.out.println("  [ERROR] Cargo exceeds max truck road limit (25,000 kg)!");
                return;
            }
            double ratePerKm = 1.85;
            double tollFee = 45.00;
            double totalCost = (distanceKm * ratePerKm) + tollFee;

            System.out.println("  [Truck Dispatch] Vehicle: Freightliner Cascadia (License: RD-9821)");
            System.out.printf("  [Route] Highway corridor: %.1f km. Total Freight Cost: $%.2f%n", distanceKm, totalCost);
            System.out.printf("  [Status] Dispatched cargo '%s' via road transport.%n", cargo.description());

        } else if (transportMode.equalsIgnoreCase("SEA")) {
            // Sea Transport logic hardcoded
            if (cargo.weightKg() > 500000) {
                System.out.println("  [ERROR] Cargo exceeds bulk vessel hold limit!");
                return;
            }
            double oceanRatePerKm = 0.45;
            double harborDues = 1200.00;
            double totalCost = (distanceKm * oceanRatePerKm) + harborDues;

            System.out.println("  [Cargo Vessel] Ship: Pacific Mariner (IMO: 9283741)");
            System.out.printf("  [Route] Oceanic freight lane: %.1f nautical km. Total Freight Cost: $%.2f%n", distanceKm, totalCost);
            System.out.printf("  [Status] Container loaded and bill of lading issued for '%s'.%n", cargo.description());

        } else {
            throw new IllegalArgumentException("Unknown transport mode: " + transportMode);
        }

        // Shared post-dispatch auditing
        System.out.printf("  [Ledger] Delivery for %s recorded in central logistics registry.%n%n", cargo.trackingId());
    }
}
