package bad;

public class BadLogisticsDemo {
    public static void main(String[] args) {
        System.out.println("=== Bad Logistics Service Demo ===");

        BadLogisticsService logistics = new BadLogisticsService();

        Cargo electronics = new Cargo("TRK-1001", "Consumer Electronics", 4200.0);
        logistics.planAndDispatch("ROAD", electronics, 620.0);

        Cargo heavyMachinery = new Cargo("SHP-8802", "Industrial Turbines", 185000.0);
        logistics.planAndDispatch("SEA", heavyMachinery, 3400.0);

        // Attempting an overweight road shipment
        Cargo massiveSteel = new Cargo("ERR-9999", "Structural Steel", 32000.0);
        logistics.planAndDispatch("ROAD", massiveSteel, 150.0);

        System.out.println("Problems Exposed:");
        System.out.println("1. All transport-specific pricing, limitations, and mechanics live in one giant file.");
        System.out.println("2. Adding 'AIR' or 'RAIL' transport requires editing BadLogisticsService.");
        System.out.println("3. Impossible to test road delivery logic independently of ocean freight.");
    }
}
