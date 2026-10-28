package com.systemdesign.lld.patterns.state.example01.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: State Pattern Vending Machine Tests")
class VendingMachineTest {

    @Test
    @DisplayName("Normal purchase cycle: NO_COIN -> HAS_COIN -> DISPENSING -> NO_COIN")
    void testNormalPurchaseCycle() {
        VendingMachine machine = new VendingMachine(3);
        assertEquals("NO_COIN", machine.getCurrentState().getStateName());

        machine.insertCoin();
        assertEquals("HAS_COIN", machine.getCurrentState().getStateName());

        machine.turnCrank();
        assertEquals("NO_COIN", machine.getCurrentState().getStateName());
        assertEquals(2, machine.getCount());
    }

    @Test
    @DisplayName("Coin ejection transitions HAS_COIN back to NO_COIN")
    void testCoinEjection() {
        VendingMachine machine = new VendingMachine(2);
        machine.insertCoin();
        assertEquals("HAS_COIN", machine.getCurrentState().getStateName());

        machine.ejectCoin();
        assertEquals("NO_COIN", machine.getCurrentState().getStateName());
        assertEquals(2, machine.getCount()); // no items dispensed
    }

    @Test
    @DisplayName("Turning crank without coin remains in NO_COIN state")
    void testTurnCrankWithoutCoin() {
        VendingMachine machine = new VendingMachine(2);
        machine.turnCrank();
        assertEquals("NO_COIN", machine.getCurrentState().getStateName());
        assertEquals(2, machine.getCount());
    }

    @Test
    @DisplayName("Inventory depletion transitions machine to SOLD_OUT state")
    void testSoldOutTransition() {
        VendingMachine machine = new VendingMachine(1);

        machine.insertCoin();
        machine.turnCrank();

        assertEquals(0, machine.getCount());
        assertEquals("SOLD_OUT", machine.getCurrentState().getStateName());

        // Operations in SOLD_OUT should not change state
        machine.insertCoin();
        assertEquals("SOLD_OUT", machine.getCurrentState().getStateName());
        machine.turnCrank();
        assertEquals("SOLD_OUT", machine.getCurrentState().getStateName());
    }

    @Test
    @DisplayName("Refilling a sold-out machine restores it to NO_COIN state")
    void testRefillRestoresNoCoinState() {
        VendingMachine machine = new VendingMachine(0);
        assertEquals("SOLD_OUT", machine.getCurrentState().getStateName());

        machine.refill(5);
        assertEquals(5, machine.getCount());
        assertEquals("NO_COIN", machine.getCurrentState().getStateName());
    }

    @Test
    @DisplayName("Inserting coin when already in HAS_COIN state leaves state unchanged")
    void testDoubleCoinInsertion() {
        VendingMachine machine = new VendingMachine(3);
        machine.insertCoin();
        assertEquals("HAS_COIN", machine.getCurrentState().getStateName());

        machine.insertCoin(); // should reject extra coin
        assertEquals("HAS_COIN", machine.getCurrentState().getStateName());
    }
}
