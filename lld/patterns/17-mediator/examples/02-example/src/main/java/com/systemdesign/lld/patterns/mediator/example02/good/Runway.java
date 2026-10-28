package com.systemdesign.lld.patterns.mediator.example02.good;

import java.util.Objects;

/**
 * Airfield runway resource managed exclusively by the ATC mediator.
 */
public class Runway {
    private final String runwayId;
    private boolean occupied;
    private Flight currentFlight;

    public Runway(String runwayId) {
        this.runwayId = Objects.requireNonNull(runwayId, "Runway ID cannot be null");
        this.occupied = false;
        this.currentFlight = null;
    }

    public synchronized boolean isOccupied() {
        return occupied;
    }

    public synchronized void assignFlight(Flight flight) {
        this.occupied = true;
        this.currentFlight = flight;
    }

    public synchronized void vacate() {
        this.occupied = false;
        this.currentFlight = null;
    }

    public String getRunwayId() {
        return runwayId;
    }

    public synchronized Flight getCurrentFlight() {
        return currentFlight;
    }

    @Override
    public String toString() {
        return String.format("Runway[%s, occupied=%s, flight=%s]", runwayId, occupied,
                currentFlight != null ? currentFlight.getFlightNumber() : "None");
    }
}
