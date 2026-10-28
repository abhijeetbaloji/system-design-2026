package com.systemdesign.lld.patterns.strategy.example02.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Strategy Pattern Route Planning Tests")
class RouteStrategyTest {

    private final Coordinate origin = new Coordinate(40.7128, -74.0060);      // NYC Downtown
    private final Coordinate destination = new Coordinate(40.7829, -73.9654); // Central Park Uptown

    @Test
    @DisplayName("DrivingRouteStrategy calculates road distance, traffic-adjusted duration and tolls")
    void testDrivingRouteStrategy() {
        DrivingRouteStrategy strategy = new DrivingRouteStrategy(60.0, 1.2, false);
        Route route = strategy.planRoute(origin, destination);

        assertTrue(route.distanceKm() > 0.0);
        assertTrue(route.durationMinutes() > 0);
        assertFalse(route.turnByTurnSteps().isEmpty());
        assertTrue(route.summary().contains("driving"));
    }

    @Test
    @DisplayName("WalkingRouteStrategy avoids tolls and provides pedestrian path")
    void testWalkingRouteStrategy() {
        WalkingRouteStrategy strategy = new WalkingRouteStrategy(5.0);
        Route route = strategy.planRoute(origin, destination);

        assertEquals(0.0, route.estimatedCost());
        assertTrue(route.durationMinutes() > 30); // walking across Manhattan takes considerable time
        assertTrue(route.summary().contains("pedestrian"));
    }

    @Test
    @DisplayName("BicyclingRouteStrategy plans greenway cycling route")
    void testBicyclingRouteStrategy() {
        BicyclingRouteStrategy strategy = new BicyclingRouteStrategy(15.0);
        Route route = strategy.planRoute(origin, destination);

        assertEquals(0.0, route.estimatedCost());
        assertTrue(route.durationMinutes() > 0);
        assertTrue(route.turnByTurnSteps().stream().anyMatch(s -> s.toLowerCase().contains("bike")));
    }

    @Test
    @DisplayName("PublicTransitRouteStrategy includes transit fare and transfer time")
    void testPublicTransitRouteStrategy() {
        PublicTransitRouteStrategy strategy = new PublicTransitRouteStrategy(2.90, 8);
        Route route = strategy.planRoute(origin, destination);

        assertEquals(2.90, route.estimatedCost());
        assertTrue(route.durationMinutes() > 8);
        assertTrue(route.summary().contains("Transit"));
    }

    @Test
    @DisplayName("Navigator allows dynamic strategy swapping at runtime")
    void testNavigatorRuntimeSwapping() {
        Navigator navigator = new Navigator(new DrivingRouteStrategy());
        Route driveRoute = navigator.planRoute(origin, destination);

        navigator.setRouteStrategy(new WalkingRouteStrategy());
        Route walkRoute = navigator.planRoute(origin, destination);

        assertNotEquals(driveRoute.durationMinutes(), walkRoute.durationMinutes());
        assertTrue(walkRoute.durationMinutes() > driveRoute.durationMinutes());
    }

    @Test
    @DisplayName("Same origin and destination yields zero distance immediately")
    void testSameCoordinate() {
        Navigator navigator = new Navigator(new DrivingRouteStrategy());
        Route route = navigator.planRoute(origin, origin);

        assertEquals(0.0, route.distanceKm());
        assertEquals(0, route.durationMinutes());
        assertEquals(0.0, route.estimatedCost());
    }

    @Test
    @DisplayName("Invalid coordinate inputs throw IllegalArgumentException")
    void testInvalidCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(100.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Coordinate(0.0, -200.0));
    }
}
