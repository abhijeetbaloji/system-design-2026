package com.systemdesign.lld.patterns.strategy.example02.good;

import java.util.Objects;

/**
 * Context Class:
 * Manages route navigation requests.
 *
 * Notice:
 * 1. Contains zero switch or if-else statements for routing logic.
 * 2. Holds a reference to the RouteStrategy interface (Composition).
 * 3. Supports hot-swapping strategies at runtime via setRouteStrategy().
 * 4. Completely decoupled from vehicle speed details, transit schedules, and toll tables.
 */
public class Navigator {

    private RouteStrategy routeStrategy;

    public Navigator(RouteStrategy routeStrategy) {
        this.routeStrategy = Objects.requireNonNull(routeStrategy, "RouteStrategy cannot be null");
    }

    public Navigator() {
        this(new DrivingRouteStrategy());
    }

    public RouteStrategy getRouteStrategy() {
        return routeStrategy;
    }

    public void setRouteStrategy(RouteStrategy routeStrategy) {
        this.routeStrategy = Objects.requireNonNull(routeStrategy, "RouteStrategy cannot be null");
    }

    /**
     * Delegates the route planning calculation to the active RouteStrategy.
     */
    public Route planRoute(Coordinate origin, Coordinate destination) {
        if (origin == null || destination == null) {
            throw new IllegalArgumentException("Origin and destination must not be null");
        }
        return routeStrategy.planRoute(origin, destination);
    }
}
