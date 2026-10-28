package com.systemdesign.lld.patterns.mediator.example02;

import com.systemdesign.lld.patterns.mediator.example02.bad.BadAirplane;
import com.systemdesign.lld.patterns.mediator.example02.bad.BadRunway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Bad ATC Uncoordinated Access Tests")
class BadAtcTest {

    @Test
    @DisplayName("Direct runway access lacks queuing and notification")
    void testUncoordinatedAccess() {
        BadRunway runway = new BadRunway("09L");
        BadAirplane flightA = new BadAirplane("UA-100");
        BadAirplane flightB = new BadAirplane("BA-200");

        assertTrue(flightA.attemptLanding(runway));
        assertTrue(runway.isOccupied());
        assertEquals("UA-100", runway.getCurrentOccupant());

        // Flight B fails and is dropped on the floor with no queue or retry
        assertFalse(flightB.attemptLanding(runway));

        flightA.vacateRunway(runway);
        assertFalse(runway.isOccupied());
        // Flight B has no automated trigger to know the runway is free
    }
}
