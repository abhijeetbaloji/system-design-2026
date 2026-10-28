package com.systemdesign.lld.patterns.strategy.example02.good;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete Strategy:
 * Implements multimodal public transportation routing:
 * - Involves first-mile walking, subway/bus transit, transfers, and last-mile walking
 * - Incorporates scheduled waiting times and standard transit fares
 */
public class PublicTransitRouteStrategy implements RouteStrategy {

    private final double transitFare;
    private final int estimatedTransferMinutes;

    public PublicTransitRouteStrategy(double transitFare, int estimatedTransferMinutes) {
        if (transitFare < 0.0) {
            throw new IllegalArgumentException("Transit fare cannot be negative");
        }
        if (estimatedTransferMinutes < 0) {
            throw new IllegalArgumentException("Transfer minutes cannot be negative");
        }
        this.transitFare = transitFare;
        this.estimatedTransferMinutes = estimatedTransferMinutes;
    }

    public PublicTransitRouteStrategy() {
        this(2.90, 10);
    }

    @Override
    public Route planRoute(Coordinate origin, Coordinate destination) {
        double straightDistance = origin.distanceTo(destination);
        if (straightDistance == 0.0) {
            return new Route(0.0, 0, 0.0, "Already at destination", List.of("Origin equals destination"));
        }

        double transitDistance = straightDistance * 1.25;
        // Average speed for municipal subway network is roughly 35 km/h
        double inTransitHours = transitDistance / 35.0;
        int inTransitMinutes = (int) Math.round(inTransitHours * 60.0);
        int totalDuration = inTransitMinutes + estimatedTransferMinutes + 8; // 8 mins first/last mile walk

        List<String> steps = new ArrayList<>();
        steps.add("Walk 350m to nearest Metro Transit Station");
        steps.add("Board Blue Line express train toward Central Station (5 stops)");
        steps.add("Transfer at Central Station (estimated wait: " + estimatedTransferMinutes + " mins)");
        steps.add("Board Silver Line Rapid Bus for " + String.format("%.1f", transitDistance * 0.4) + " km");
        steps.add("Alight at Grand Avenue stop and walk 120m to destination");

        return new Route(transitDistance, totalDuration, transitFare, "Multimodal Public Transit: Metro + Bus", steps);
    }
}
