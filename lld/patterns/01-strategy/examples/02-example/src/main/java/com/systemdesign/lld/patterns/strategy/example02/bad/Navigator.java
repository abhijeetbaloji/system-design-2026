package com.systemdesign.lld.patterns.strategy.example02.bad;

import java.util.ArrayList;
import java.util.List;

/**
 * BAD DESIGN:
 * Monolithic Navigator class containing all routing calculations inside
 * a giant switch statement.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Adding a new travel mode (e.g., Electric Scooter, Ferry, RideShare) requires
 *    modifying this Navigator class and touching existing stable routing logic.
 * 2. Violates Single Responsibility Principle (SRP):
 *    This class contains highway traffic algorithms, pedestrian footpath rules,
 *    bike elevation calculations, and transit schedules all jammed together.
 * 3. Fragile & Rigid:
 *    Tightly coupled to all mode specifics. Cannot test pedestrian routing without
 *    loading highway and transit dependencies.
 */
public class Navigator {

    public Route buildRoute(Coordinate origin, Coordinate destination, TravelMode mode) {
        if (origin == null || destination == null || mode == null) {
            throw new IllegalArgumentException("Origin, destination, and mode must not be null");
        }

        double baseDistance = origin.distanceTo(destination);
        List<String> directions = new ArrayList<>();

        switch (mode) {
            case DRIVING:
                // Driving uses highways, which can be slightly longer in distance but faster
                double driveDistance = baseDistance * 1.15;
                // Average speed 60 km/h with 1.2x traffic multiplier
                int driveDuration = (int) Math.round((driveDistance / 60.0) * 60.0 * 1.2);
                double tollCost = driveDistance > 15.0 ? 5.50 : 0.0;
                directions.add("Head north toward Highway 101");
                directions.add("Take Highway 101 for " + String.format("%.1f", driveDistance * 0.7) + " km");
                directions.add("Take exit 24B and merge onto arterial road");
                directions.add("Arrive at destination on the right");
                return new Route(driveDistance, driveDuration, tollCost, "Fastest driving route via Highway 101", directions);

            case WALKING:
                // Walking uses short direct pedestrian paths and sidewalks
                double walkDistance = baseDistance * 1.05;
                // Average pedestrian speed 4.8 km/h
                int walkDuration = (int) Math.round((walkDistance / 4.8) * 60.0);
                directions.add("Head east on pedestrian sidewalk");
                directions.add("Cut through Central Park walkway for " + String.format("%.1f", walkDistance * 0.4) + " km");
                directions.add("Cross at the pedestrian crosswalk");
                directions.add("Destination is ahead");
                return new Route(walkDistance, walkDuration, 0.0, "Scenic pedestrian walking route", directions);

            case BICYCLING:
                // Bicycling uses bike lanes and greenways, avoiding steep hills
                double bikeDistance = baseDistance * 1.10;
                // Average cycling speed 16 km/h
                int bikeDuration = (int) Math.round((bikeDistance / 16.0) * 60.0);
                directions.add("Join designated bike lane on 5th Avenue");
                directions.add("Follow protected cycle track along Riverside Greenway for " + String.format("%.1f", bikeDistance * 0.6) + " km");
                directions.add("Turn left onto neighborhood street bike path");
                directions.add("Arrive at bike docking station near destination");
                return new Route(bikeDistance, bikeDuration, 0.0, "Safe cycling route via protected bike lanes", directions);

            case PUBLIC_TRANSIT:
                // Transit involves walking to station, train ride, and final walking segment
                double transitDistance = baseDistance * 1.25;
                // Fixed transit interval + train journey
                int transitDuration = (int) Math.round((transitDistance / 35.0) * 60.0) + 12; // 12 min wait & transfer
                double fare = 2.75;
                directions.add("Walk 400m to Metro Line A Station");
                directions.add("Board uptown express subway train (4 stops)");
                directions.add("Transfer to Crosstown Bus Line 14");
                directions.add("Disembark at destination stop and walk 150m");
                return new Route(transitDistance, transitDuration, fare, "Public transit: Subway + Bus", directions);

            default:
                throw new UnsupportedOperationException("Unsupported travel mode: " + mode);
        }
    }
}
