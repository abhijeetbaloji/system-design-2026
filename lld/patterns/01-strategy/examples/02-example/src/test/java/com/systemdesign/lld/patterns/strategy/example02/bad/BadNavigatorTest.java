package com.systemdesign.lld.patterns.strategy.example02.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Monolithic Navigator Tests")
class BadNavigatorTest {

    @Test
    @DisplayName("Monolithic switch executes different travel modes")
    void testMonolithicRouting() {
        Coordinate origin = new Coordinate(40.7128, -74.0060);
        Coordinate dest = new Coordinate(40.7829, -73.9654);
        Navigator navigator = new Navigator();

        Route drive = navigator.buildRoute(origin, dest, TravelMode.DRIVING);
        assertNotNull(drive);
        assertTrue(drive.distanceKm() > 0);

        Route walk = navigator.buildRoute(origin, dest, TravelMode.WALKING);
        assertNotNull(walk);
        assertTrue(walk.durationMinutes() > drive.durationMinutes());
    }

    @Test
    @DisplayName("Null parameters throw IllegalArgumentException")
    void testNullValidation() {
        Navigator navigator = new Navigator();
        Coordinate c = new Coordinate(0.0, 0.0);
        assertThrows(IllegalArgumentException.class, () -> navigator.buildRoute(null, c, TravelMode.DRIVING));
    }
}
