package com.systemdesign.lld.patterns.mediator.example02.good;

import java.util.List;

/**
 * Mediator interface declaring operations for air traffic management.
 */
public interface AirTrafficControlMediator {
    void registerFlight(Flight flight);

    void registerRunway(Runway runway);

    boolean requestLandingClearance(Flight flight);

    boolean requestTakeoffClearance(Flight flight);

    void notifyRunwayVacated(Flight flight, Runway runway);

    List<String> getEventLog();
}
