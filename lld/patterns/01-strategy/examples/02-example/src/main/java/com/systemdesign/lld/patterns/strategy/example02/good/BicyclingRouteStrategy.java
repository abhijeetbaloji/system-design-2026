package com.systemdesign.lld.patterns.strategy.example02.good;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Strategy:
 * Implements cycling routing:
 * - Prioritizes dedicated bike lanes, greenways, and residential boulevards
 * - Avoids highways and steep elevation climbs where possible
 * - Average cycling pace: ~16 km/h
 */
public class BicyclingRouteStrategy implements RouteStrategy {

    private final double cyclingSpeedKmh;

    public BicyclingRouteStrategy(double cyclingSpeedKmh) {
        if (cyclingSpeedKmh <= 0) {
            throw new IllegalArgumentException("Cycling speed must be positive");
        }
        this.cyclingSpeedKmh = cyclingSpeedKmh;
    }

    public BicyclingRouteStrategy() {
        this(16.0);
    }

    @Override
    public Route planRoute(Coordinate origin, Coordinate destination) {
        double straightDistance = origin.distanceTo(destination);
        if (straightDistance == 0.0) {
            return new Route(0.0, 0, 0.0, "Already at destination", List.of("Origin equals destination"));
        }

        double cyclingDistance = straightDistance * 1.10;
        double hours = cyclingDistance / cyclingSpeedKmh;
        int durationMinutes = Math.max(1, (int) Math.round(hours * 60.0));

        List<String> steps = new ArrayList<>();
        steps.add("Mount bicycle and turn onto local bike lane");
        steps.add("Follow protected waterfront cycling trail for " + String.format("%.1f", cyclingDistance * 0.6) + " km");
        steps.add("Turn left onto residential bike boulevard (speed limit 25 km/h)");
        steps.add("Arrive at municipal bike parking rack near destination");

        return new Route(cyclingDistance, durationMinutes, 0.0, "Safe cycling route via greenway", steps);
    }
}
