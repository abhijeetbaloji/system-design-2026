package com.systemdesign.lld.patterns.state.example01.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Monolithic Vending Machine Tests")
class BadVendingMachineTest {

    @Test
    @DisplayName("Monolithic switch executes basic transitions")
    void testBasicTransitions() {
        VendingMachine vm = new VendingMachine(2);
        assertEquals(State.NO_COIN, vm.getState());

        vm.insertCoin();
        assertEquals(State.HAS_COIN, vm.getState());

        vm.turnCrank();
        assertEquals(State.NO_COIN, vm.getState());
        assertEquals(1, vm.getCount());
    }

    @Test
    @DisplayName("Demonstrates sold out condition")
    void testSoldOut() {
        VendingMachine vm = new VendingMachine(1);
        vm.insertCoin();
        vm.turnCrank();
        assertEquals(State.SOLD_OUT, vm.getState());
    }
}
