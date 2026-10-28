package com.systemdesign.lld.patterns.mediator.example02.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Abstract Colleague representing an aircraft. Communicates strictly with
 * the AirTrafficControlMediator, with zero references to runways or other flights.
 */
public abstract class Flight {
    protected final AirTrafficControlMediator atc;
    protected final String flightNumber;
    protected final String airline;
    protected FlightStatus status;
    protected Runway assignedRunway;
    protected final List<String> pilotLogs = new ArrayList<>();

    public Flight(AirTrafficControlMediator atc, String flightNumber, String airline, FlightStatus initialStatus) {
        this.atc = Objects.requireNonNull(atc, "ATC Mediator cannot be null");
        this.flightNumber = Objects.requireNonNull(flightNumber, "Flight number cannot be null");
        this.airline = Objects.requireNonNull(airline, "Airline cannot be null");
        this.status = Objects.requireNonNull(initialStatus, "Initial status cannot be null");
    }

    public boolean requestLanding() {
        pilotLogs.add(String.format("[%s] Requesting landing clearance from ATC.", flightNumber));
        return atc.requestLandingClearance(this);
    }

    public void land() {
        if (assignedRunway == null) {
            throw new IllegalStateException("Cannot land without assigned runway!");
        }
        this.status = FlightStatus.LANDING;
        pilotLogs.add(String.format("[%s] Touchdown confirmed on %s.", flightNumber, assignedRunway.getRunwayId()));
        // Taxi off and notify tower
        this.status = FlightStatus.ON_GROUND;
        Runway usedRunway = this.assignedRunway;
        this.assignedRunway = null;
        pilotLogs.add(String.format("[%s] Vacated runway %s. Taxied to gate.", flightNumber, usedRunway.getRunwayId()));
        atc.notifyRunwayVacated(this, usedRunway);
    }

    public boolean requestTakeoff() {
        pilotLogs.add(String.format("[%s] Requesting takeoff clearance from ATC.", flightNumber));
        return atc.requestTakeoffClearance(this);
    }

    public void takeOff() {
        if (assignedRunway == null) {
            throw new IllegalStateException("Cannot takeoff without assigned runway!");
        }
        this.status = FlightStatus.TAKEOFF;
        pilotLogs.add(String.format("[%s] Airborne from %s.", flightNumber, assignedRunway.getRunwayId()));
        this.status = FlightStatus.IN_FLIGHT;
        Runway usedRunway = this.assignedRunway;
        this.assignedRunway = null;
        pilotLogs.add(String.format("[%s] Cleared airspace from %s.", flightNumber, usedRunway.getRunwayId()));
        atc.notifyRunwayVacated(this, usedRunway);
    }

    public void receiveClearance(String message, Runway runway) {
        this.assignedRunway = runway;
        pilotLogs.add(String.format("[%s] CLEARANCE RECEIVED from ATC: %s (Runway: %s)",
                flightNumber, message, runway != null ? runway.getRunwayId() : "Pending"));
    }

    public void updateStatus(FlightStatus status) {
        this.status = status;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public Runway getAssignedRunway() {
        return assignedRunway;
    }

    public List<String> getPilotLogs() {
        return Collections.unmodifiableList(pilotLogs);
    }

    @Override
    public String toString() {
        return String.format("%s %s [%s]", airline, flightNumber, status);
    }
}
