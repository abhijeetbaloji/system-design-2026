package com.systemdesign.lld.patterns.mediator.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Concrete Mediator implementation coordinating air traffic operations.
 * Enforces runway mutual exclusion, manages landing and takeoff queues,
 * and maintains airport audit logs.
 */
public class AtcTower implements AirTrafficControlMediator {
    private final String towerName;
    private final Map<String, Runway> runways = new ConcurrentHashMap<>();
    private final Map<String, Flight> registeredFlights = new ConcurrentHashMap<>();
    private final Queue<Flight> landingHoldingPattern = new LinkedList<>();
    private final Queue<Flight> takeoffQueue = new LinkedList<>();
    private final List<String> eventLog = Collections.synchronizedList(new ArrayList<>());

    public AtcTower(String towerName) {
        this.towerName = Objects.requireNonNull(towerName, "Tower name cannot be null");
    }

    @Override
    public void registerFlight(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        registeredFlights.put(flight.getFlightNumber(), flight);
        logEvent(String.format("FLIGHT_REGISTERED: %s", flight));
    }

    @Override
    public void registerRunway(Runway runway) {
        Objects.requireNonNull(runway, "Runway cannot be null");
        runways.put(runway.getRunwayId(), runway);
        logEvent(String.format("RUNWAY_REGISTERED: %s", runway.getRunwayId()));
    }

    @Override
    public synchronized boolean requestLandingClearance(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        Runway freeRunway = findAvailableRunway();

        if (freeRunway != null) {
            freeRunway.assignFlight(flight);
            flight.receiveClearance("CLEARED TO LAND", freeRunway);
            logEvent(String.format("CLEARANCE_GRANTED: %s cleared to land on %s",
                    flight.getFlightNumber(), freeRunway.getRunwayId()));
            return true;
        } else {
            flight.updateStatus(FlightStatus.HOLDING_LANDING);
            landingHoldingPattern.add(flight);
            flight.receiveClearance("HOLD IN AIRSPACE - ALL RUNWAYS OCCUPIED", null);
            logEvent(String.format("HOLDING_ASSIGNED: %s entered landing holding pattern (Position #%d)",
                    flight.getFlightNumber(), landingHoldingPattern.size()));
            return false;
        }
    }

    @Override
    public synchronized boolean requestTakeoffClearance(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        Runway freeRunway = findAvailableRunway();

        if (freeRunway != null) {
            freeRunway.assignFlight(flight);
            flight.receiveClearance("CLEARED FOR TAKEOFF", freeRunway);
            logEvent(String.format("CLEARANCE_GRANTED: %s cleared for takeoff on %s",
                    flight.getFlightNumber(), freeRunway.getRunwayId()));
            return true;
        } else {
            flight.updateStatus(FlightStatus.HOLDING_TAKEOFF);
            takeoffQueue.add(flight);
            flight.receiveClearance("HOLD ON TAXIWAY - ALL RUNWAYS OCCUPIED", null);
            logEvent(String.format("HOLDING_ASSIGNED: %s entered takeoff queue (Position #%d)",
                    flight.getFlightNumber(), takeoffQueue.size()));
            return false;
        }
    }

    @Override
    public synchronized void notifyRunwayVacated(Flight flight, Runway runway) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        Objects.requireNonNull(runway, "Runway cannot be null");

        runway.vacate();
        logEvent(String.format("RUNWAY_VACATED: %s vacated %s",
                flight.getFlightNumber(), runway.getRunwayId()));

        // Priority 1: Inbound landing aircraft in holding pattern
        if (!landingHoldingPattern.isEmpty()) {
            Flight nextFlight = landingHoldingPattern.poll();
            runway.assignFlight(nextFlight);
            nextFlight.receiveClearance("CLEARED TO LAND AFTER HOLD", runway);
            logEvent(String.format("CLEARANCE_PROMOTED: %s cleared to land on %s from holding pattern",
                    nextFlight.getFlightNumber(), runway.getRunwayId()));
        }
        // Priority 2: Outbound takeoff aircraft on taxiway
        else if (!takeoffQueue.isEmpty()) {
            Flight nextFlight = takeoffQueue.poll();
            runway.assignFlight(nextFlight);
            nextFlight.receiveClearance("CLEARED FOR TAKEOFF AFTER HOLD", runway);
            logEvent(String.format("CLEARANCE_PROMOTED: %s cleared for takeoff on %s from taxiway queue",
                    nextFlight.getFlightNumber(), runway.getRunwayId()));
        }
    }

    private Runway findAvailableRunway() {
        for (Runway runway : runways.values()) {
            if (!runway.isOccupied()) {
                return runway;
            }
        }
        return null;
    }

    private void logEvent(String event) {
        eventLog.add(String.format("[%s] %s", towerName, event));
    }

    @Override
    public List<String> getEventLog() {
        return Collections.unmodifiableList(new ArrayList<>(eventLog));
    }

    public int getHoldingPatternCount() {
        return landingHoldingPattern.size();
    }

    public int getTakeoffQueueCount() {
        return takeoffQueue.size();
    }

    public String getTowerName() {
        return towerName;
    }
}
