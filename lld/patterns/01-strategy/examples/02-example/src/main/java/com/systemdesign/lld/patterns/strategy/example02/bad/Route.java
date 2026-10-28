package com.systemdesign.lld.patterns.strategy.example02.bad;

import java.util.List;

/**
 * Resulting navigation route.
 */
public record Route(double distanceKm, int durationMinutes, double estimatedCost, String summary, List<String> steps) {
}
