package com.systemdesign.lld.patterns.strategy.example02.good;

import java.util.List;

/**
 * Immutable route value object describing the itinerary.
 */
public record Route(
    double distanceKm,
    int durationMinutes,
    double estimatedCost,
    String summary,
    List<String> turnByTurnSteps
) {
    public Route {
        if (distanceKm < 0.0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        if (durationMinutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        if (estimatedCost < 0.0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        turnByTurnSteps = turnByTurnSteps != null ? List.copyOf(turnByTurnSteps) : List.of();
    }
}
