package com.systemdesign.lld.patterns.state.example01.good;

import java.util.Objects;

/**
 * Context Class:
 * Represents the physical Vending Machine hardware.
 *
 * Notice:
 * 1. Contains zero switch or if-else statements for state transitions.
 * 2. Maintains pre-allocated concrete state objects to avoid garbage collection.
 * 3. Delegates all user actions to the active currentState.
 */
public class VendingMachine {

    // Concrete State instances managed by this context
    private final VendingState noCoinState;
    private final VendingState hasCoinState;
    private final VendingState dispensingState;
    private final VendingState soldOutState;

    private VendingState currentState;
    private int count;

    public VendingMachine(int initialCount) {
        this.count = Math.max(0, initialCount);

        this.noCoinState = new NoCoinState(this);
        this.hasCoinState = new HasCoinState(this);
        this.dispensingState = new DispensingState(this);
        this.soldOutState = new SoldOutState(this);

        this.currentState = this.count > 0 ? this.noCoinState : this.soldOutState;
    }

    public void insertCoin() {
        currentState.insertCoin();
    }

    public void ejectCoin() {
        currentState.ejectCoin();
    }

    public void turnCrank() {
        currentState.turnCrank();
        currentState.dispense();
    }

    public void setState(VendingState newState) {
        this.currentState = Objects.requireNonNull(newState, "State cannot be null");
    }

    public void releaseItem() {
        if (count > 0) {
            count--;
            System.out.println("Hardware: Snack item dropped into collection tray.");
        }
    }

    public void refill(int additionalCount) {
        if (additionalCount <= 0) {
            throw new IllegalArgumentException("Refill quantity must be positive");
        }
        this.count += additionalCount;
        System.out.println("Restocked with " + additionalCount + " items. Total: " + count);
        if (currentState == soldOutState) {
            this.currentState = noCoinState;
        }
    }

    public VendingState getCurrentState() { return currentState; }
    public VendingState getNoCoinState() { return noCoinState; }
    public VendingState getHasCoinState() { return hasCoinState; }
    public VendingState getDispensingState() { return dispensingState; }
    public VendingState getSoldOutState() { return soldOutState; }
    public int getCount() { return count; }
}
