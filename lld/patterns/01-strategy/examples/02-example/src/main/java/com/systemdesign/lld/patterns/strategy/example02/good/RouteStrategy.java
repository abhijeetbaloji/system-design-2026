package com.systemdesign.lld.patterns.strategy.example02.good;

/**
 * Strategy Interface:
 * Declares the contract for all routing algorithms (e.g., Driving, Walking, Transit).
 */
@FunctionalInterface
public interface RouteStrategy {

    /**
     * Calculates the optimal route between origin and destination coordinates.
     *
     * @param origin departure coordinate
     * @param destination arrival coordinate
     * @return a fully populated Route itinerary
     */
    Route planRoute(Coordinate origin, Coordinate destination);
}
