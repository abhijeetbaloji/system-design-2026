package com.systemdesign.lld.patterns.mediator.example02.good;

import java.util.Objects;

/**
 * Service demonstrating orchestration of ATC airspace operations.
 */
public class AirTrafficControlService {
    private final AirTrafficControlMediator mediator;

    public AirTrafficControlService(AirTrafficControlMediator mediator) {
        this.mediator = Objects.requireNonNull(mediator, "Mediator cannot be null");
    }

    public void setupRunway(String runwayId) {
        mediator.registerRunway(new Runway(runwayId));
    }

    public void registerInboundFlight(Flight flight) {
        mediator.registerFlight(flight);
    }

    public void printAirspaceLogs() {
        System.out.println("=================================================");
        System.out.println(" AIR TRAFFIC CONTROL TOWER LOGS");
        System.out.println("=================================================");
        for (String log : mediator.getEventLog()) {
            System.out.println(" * " + log);
        }
        System.out.println("=================================================");
    }
}
