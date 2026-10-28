package com.systemdesign.lld.patterns.mediator.example02;

import com.systemdesign.lld.patterns.mediator.example02.good.AtcTower;
import com.systemdesign.lld.patterns.mediator.example02.good.CargoFlight;
import com.systemdesign.lld.patterns.mediator.example02.good.CommercialFlight;
import com.systemdesign.lld.patterns.mediator.example02.good.FlightStatus;
import com.systemdesign.lld.patterns.mediator.example02.good.Runway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ATC Tower Mediator Pattern Tests")
class AtcTowerTest {

    private AtcTower tower;
    private Runway runway1;
    private Runway runway2;
    private CommercialFlight flightA;
    private CommercialFlight flightB;
    private CommercialFlight flightC;
    private CargoFlight cargoFlight;

    @BeforeEach
    void setUp() {
        tower = new AtcTower("Test-Tower");
        runway1 = new Runway("09L");
        runway2 = new Runway("09R");
        tower.registerRunway(runway1);
        tower.registerRunway(runway2);

        flightA = new CommercialFlight(tower, "UA-100", "United", 150);
        flightB = new CommercialFlight(tower, "BA-200", "British Airways", 200);
        flightC = new CommercialFlight(tower, "LH-300", "Lufthansa", 180);
        cargoFlight = new CargoFlight(tower, "FX-400", "FedEx", 30.0);

        tower.registerFlight(flightA);
        tower.registerFlight(flightB);
        tower.registerFlight(flightC);
        tower.registerFlight(cargoFlight);
    }

    @Test
    @DisplayName("Immediate clearance granted when runways are available")
    void testImmediateClearance() {
        assertTrue(flightA.requestLanding());
        assertNotNull(flightA.getAssignedRunway());

        assertTrue(flightB.requestLanding());
        assertNotNull(flightB.getAssignedRunway());

        // Both runways now occupied
        assertTrue(runway1.isOccupied());
        assertTrue(runway2.isOccupied());
    }

    @Test
    @DisplayName("Aircraft enters holding pattern when all runways are occupied")
    void testHoldingPatternWhenFull() {
        flightA.requestLanding();
        flightB.requestLanding();

        // 3rd flight must hold
        boolean cleared = flightC.requestLanding();
        assertFalse(cleared);
        assertEquals(FlightStatus.HOLDING_LANDING, flightC.getStatus());
        assertNull(flightC.getAssignedRunway());
        assertEquals(1, tower.getHoldingPatternCount());
    }

    @Test
    @DisplayName("Vacating runway automatically promotes flight from holding pattern")
    void testHoldingPromotionOnVacate() {
        flightA.requestLanding();
        flightB.requestLanding();
        flightC.requestLanding(); // Enters holding

        assertEquals(1, tower.getHoldingPatternCount());
        assertNull(flightC.getAssignedRunway());

        // Flight A finishes landing and taxies away
        flightA.land();

        // Tower should have promoted Flight C
        assertEquals(0, tower.getHoldingPatternCount());
        assertNotNull(flightC.getAssignedRunway());
        assertEquals(FlightStatus.ON_GROUND, flightA.getStatus());

        // Flight C can now land safely
        flightC.land();
        assertEquals(FlightStatus.ON_GROUND, flightC.getStatus());
    }

    @Test
    @DisplayName("Takeoff requests enter queue when full and execute when promoted")
    void testTakeoffQueue() {
        flightA.requestLanding();
        flightB.requestLanding();

        // Cargo flight wants takeoff, but runways full
        assertFalse(cargoFlight.requestTakeoff());
        assertEquals(FlightStatus.HOLDING_TAKEOFF, cargoFlight.getStatus());
        assertEquals(1, tower.getTakeoffQueueCount());

        // Flight A vacates runway
        flightA.land();

        // Cargo flight promoted for takeoff
        assertEquals(0, tower.getTakeoffQueueCount());
        assertNotNull(cargoFlight.getAssignedRunway());

        cargoFlight.takeOff();
        assertEquals(FlightStatus.IN_FLIGHT, cargoFlight.getStatus());
    }

    @Test
    @DisplayName("Landing without assigned runway throws IllegalStateException")
    void testLandingWithoutRunwayThrows() {
        CommercialFlight unassignedFlight = new CommercialFlight(tower, "UN-999", "Unknown", 50);
        assertThrows(IllegalStateException.class, unassignedFlight::land);
    }

    @Test
    @DisplayName("Tower records complete audit log of all clearances and transitions")
    void testAuditLog() {
        flightA.requestLanding();
        flightA.land();

        List<String> logs = tower.getEventLog();
        assertTrue(logs.stream().anyMatch(l -> l.contains("CLEARANCE_GRANTED: UA-100")));
        assertTrue(logs.stream().anyMatch(l -> l.contains("RUNWAY_VACATED: UA-100")));
    }
}
