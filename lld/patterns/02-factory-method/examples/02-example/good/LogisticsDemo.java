package good;

/**
 * Client Demonstration.
 *
 * Demonstrates how client applications interact with the abstract Logistics service.
 * The core planning workflow handles validation, capacity checks, and auditing polymorphically.
 */
public class LogisticsDemo {

    public static void main(String[] args) {
        System.out.println("=== Factory Method Pattern: Logistics Management Demo ===\n");

        // 1. Road Logistics Scenario
        Logistics roadLogistics = new RoadLogistics("SCANIA-R500-US", 24000.0);
        Cargo pallets = new Cargo("CRG-2001", "Medical Diagnostic Supplies", 5200.0);
        roadLogistics.planDelivery(pallets, 480.0);

        // 2. Sea Logistics Scenario
        Logistics seaLogistics = new SeaLogistics("Evergreen Pioneer", "IMO-9811002", 600000.0);
        Cargo heavyMiningEquipment = new Cargo("CRG-8092", "Excavator Drill Assemblies", 140000.0);
        seaLogistics.planDelivery(heavyMiningEquipment, 5200.0);

        // 3. Road Logistics Capacity Exceeded Scenario
        Cargo oversizedTransformers = new Cargo("CRG-9999", "Grid Power Transformers", 48000.0);
        roadLogistics.planDelivery(oversizedTransformers, 120.0);
    }
}
