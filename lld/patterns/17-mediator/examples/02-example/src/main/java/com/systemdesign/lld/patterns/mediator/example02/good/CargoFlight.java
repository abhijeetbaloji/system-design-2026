package com.systemdesign.lld.patterns.mediator.example02.good;

/**
 * Concrete colleague representing cargo transport aircraft.
 */
public class CargoFlight extends Flight {
    private final double cargoWeightTons;

    public CargoFlight(AirTrafficControlMediator atc, String flightNumber, String airline, double cargoWeightTons) {
        super(atc, flightNumber, airline, FlightStatus.ON_GROUND);
        this.cargoWeightTons = cargoWeightTons;
    }

    public double getCargoWeightTons() {
        return cargoWeightTons;
    }
}
