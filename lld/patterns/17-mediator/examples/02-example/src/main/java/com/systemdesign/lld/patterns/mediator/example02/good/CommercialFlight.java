package com.systemdesign.lld.patterns.mediator.example02.good;

/**
 * Concrete colleague representing passenger commercial aircraft.
 */
public class CommercialFlight extends Flight {
    private final int passengerCount;

    public CommercialFlight(AirTrafficControlMediator atc, String flightNumber, String airline, int passengerCount) {
        super(atc, flightNumber, airline, FlightStatus.IN_FLIGHT);
        this.passengerCount = passengerCount;
    }

    public int getPassengerCount() {
        return passengerCount;
    }
}
