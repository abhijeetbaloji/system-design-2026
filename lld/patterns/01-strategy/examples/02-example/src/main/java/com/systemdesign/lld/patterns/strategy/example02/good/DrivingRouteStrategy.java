package com.systemdesign.lld.patterns.strategy.example02.good;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Strategy:
 * Implements high-speed vehicular routing utilizing arterial roads and highways.
 * Factors in traffic congestion and highway toll fees.
 */
public class DrivingRouteStrategy implements RouteStrategy {

    private final double averageSpeedKmh;
    private final double trafficMultiplier; // 1.0 = clear, 1.5 = heavy traffic
    private final boolean avoidTolls;

    public DrivingRouteStrategy(double averageSpeedKmh, double trafficMultiplier, boolean avoidTolls) {
        if (averageSpeedKmh <= 0) {
            throw new IllegalArgumentException("Speed must be positive");
        }
        if (trafficMultiplier < 1.0) {
            throw new IllegalArgumentException("Traffic multiplier cannot be less than 1.0");
        }
        this.averageSpeedKmh = averageSpeedKmh;
        this.trafficMultiplier = trafficMultiplier;
        this.avoidTolls = avoidTolls;
    }

    public DrivingRouteStrategy() {
        this(60.0, 1.15, false);
    }

    @Override
    public Route planRoute(Coordinate origin, Coordinate destination) {
        double straightDistance = origin.distanceTo(destination);
        if (straightDistance == 0.0) {
            return new Route(0.0, 0, 0.0, "Already at destination", List.of("Origin equals destination"));
        }

        // Driving distance is typically ~15% longer than straight line due to road grids
        double drivingDistance = straightDistance * 1.15;
        // Travel duration in minutes with traffic adjustment
        double hours = (drivingDistance / averageSpeedKmh) * trafficMultiplier;
        int durationMinutes = Math.max(1, (int) Math.round(hours * 60.0));

        double tollFee = (!avoidTolls && drivingDistance > 12.0) ? 6.50 : 0.0;

        List<String> steps = new ArrayList<>();
        steps.add("Proceed toward arterial highway on-ramp");
        steps.add("Merge onto Highway I-95 for " + String.format("%.1f", drivingDistance * 0.75) + " km");
        if (tollFee > 0) {
            steps.add("Pass electronic toll gantry ($" + String.format("%.2f", tollFee) + ")");
        }
        steps.add("Take exit toward downtown arterial avenue");
        steps.add("Turn right and arrive at destination on right");

        String summary = avoidTolls ? "Fast driving route (tolls avoided)" : "Optimal driving route via I-95";
        return new Route(drivingDistance, durationMinutes, tollFee, summary, steps);
    }
}
