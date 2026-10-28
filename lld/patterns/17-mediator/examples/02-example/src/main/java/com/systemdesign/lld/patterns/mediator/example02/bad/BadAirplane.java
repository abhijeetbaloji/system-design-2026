package com.systemdesign.lld.patterns.mediator.example02.bad;

/**
 * Flawed design: Airplane directly negotiates with Runway and peer aircraft.
 * Lacks central coordination and holding queue.
 */
public class BadAirplane {
    private final String flightNumber;

    public BadAirplane(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean attemptLanding(BadRunway runway) {
        // Race condition / TOCTOU hazard: check then act without central synchronization
        if (!runway.isOccupied()) {
            runway.setOccupied(true, flightNumber);
            return true;
        }
        return false;
    }

    public void vacateRunway(BadRunway runway) {
        if (flightNumber.equals(runway.getCurrentOccupant())) {
            runway.setOccupied(false, null);
        }
    }

    public String getFlightNumber() {
        return flightNumber;
    }
}
