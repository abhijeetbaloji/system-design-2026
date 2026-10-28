package com.systemdesign.lld.patterns.mediator.example02.good;

public class AtcSimulationDemo {
    public static void main(String[] args) {
        AtcTower tower = new AtcTower("JFK-International-Tower");
        AirTrafficControlService service = new AirTrafficControlService(tower);

        // Setup 2 runways
        Runway runway04L = new Runway("RWY-04L");
        Runway runway04R = new Runway("RWY-04R");
        tower.registerRunway(runway04L);
        tower.registerRunway(runway04R);

        // Flights arriving and departing
        CommercialFlight ua101 = new CommercialFlight(tower, "UA-101", "United Airlines", 180);
        CommercialFlight ba202 = new CommercialFlight(tower, "BA-202", "British Airways", 240);
        CommercialFlight dl303 = new CommercialFlight(tower, "DL-303", "Delta Air Lines", 210);
        CargoFlight fx404 = new CargoFlight(tower, "FX-404", "FedEx Cargo", 45.5);

        tower.registerFlight(ua101);
        tower.registerFlight(ba202);
        tower.registerFlight(dl303);
        tower.registerFlight(fx404);

        System.out.println("--- Simultaneous landing requests ---");
        ua101.requestLanding(); // Gets 04L or 04R
        ba202.requestLanding(); // Gets the other runway
        dl303.requestLanding(); // All runways occupied -> enters holding pattern!

        System.out.println("--- Outbound takeoff request ---");
        fx404.requestTakeoff(); // All runways occupied -> enters taxiway queue!

        System.out.println("--- UA-101 completes landing and vacates runway ---");
        ua101.land(); // Vacates runway, which automatically clears DL-303 from holding pattern!

        System.out.println("--- DL-303 lands on newly cleared runway ---");
        dl303.land(); // Vacates runway, which automatically clears FX-404 for takeoff!

        System.out.println("--- FX-404 takes off ---");
        fx404.takeOff();

        service.printAirspaceLogs();
    }
}
