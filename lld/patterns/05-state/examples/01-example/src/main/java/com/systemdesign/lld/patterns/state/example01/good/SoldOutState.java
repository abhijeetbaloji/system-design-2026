package com.systemdesign.lld.patterns.state.example01.good;

/**
 * Concrete State: The machine inventory is completely exhausted.
 */
public class SoldOutState implements VendingState {

    private final VendingMachine machine;

    public SoldOutState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("You cannot insert a coin; the machine is sold out.");
    }

    @Override
    public void ejectCoin() {
        System.out.println("You cannot eject; you have not inserted a coin.");
    }

    @Override
    public void turnCrank() {
        System.out.println("You turned the crank, but there are no items left.");
    }

    @Override
    public void dispense() {
        System.out.println("No item dispensed; inventory is empty.");
    }

    @Override
    public String getStateName() {
        return "SOLD_OUT";
    }
}
