package com.systemdesign.lld.patterns.strategy.example02.good;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Strategy:
 * Implements pedestrian routing:
 * - Uses sidewalks, public parks, and pedestrian passages
 * - Strictly avoids highways, freeways, and vehicular tolls
 * - Average walking pace: ~4.8 km/h
 */
public class WalkingRouteStrategy implements RouteStrategy {

    private final double walkingSpeedKmh;

    public WalkingRouteStrategy(double walkingSpeedKmh) {
        if (walkingSpeedKmh <= 0) {
            throw new IllegalArgumentException("Walking speed must be positive");
        }
        this.walkingSpeedKmh = walkingSpeedKmh;
    }

    public WalkingRouteStrategy() {
        this(4.8);
    }

    @Override
    public Route planRoute(Coordinate origin, Coordinate destination) {
        double straightDistance = origin.distanceTo(destination);
        if (straightDistance == 0.0) {
            return new Route(0.0, 0, 0.0, "Already at destination", List.of("Origin equals destination"));
        }

        // Pedestrian walkways can cut through parks and plazas, roughly 5% over straight line
        double walkingDistance = straightDistance * 1.05;
        double hours = walkingDistance / walkingSpeedKmh;
        int durationMinutes = Math.max(1, (int) Math.round(hours * 60.0));

        List<String> steps = new ArrayList<>();
        steps.add("Exit building and proceed south on pedestrian sidewalk");
        steps.add("Enter park plaza and follow designated stone footpath for " + String.format("%.1f", walkingDistance * 0.45) + " km");
        steps.add("Use signalized pedestrian crosswalk at Maple Avenue");
        steps.add("Walk 200m down tree-lined promenade");
        steps.add("Arrive at destination entrance");

        return new Route(walkingDistance, durationMinutes, 0.0, "Scenic pedestrian walking route", steps);
    }
}
