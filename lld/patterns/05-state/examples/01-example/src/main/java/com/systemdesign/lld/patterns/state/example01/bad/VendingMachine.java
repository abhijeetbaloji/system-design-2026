package com.systemdesign.lld.patterns.state.example01.bad;

/**
 * BAD DESIGN:
 * A monolithic VendingMachine using switch statements across all actions.
 *
 * Flaws:
 * 1. Violates Open/Closed Principle (OCP):
 *    Adding a new state (e.g., WINNER_FREE_ITEM, MAINTENANCE, CARD_READER_ACTIVE)
 *    requires editing every single method in this file.
 * 2. Violates Single Responsibility Principle (SRP):
 *    Manages physical inventory, hardware communication, and the combinatorial matrix
 *    of state transition rules.
 * 3. High Risk of Inconsistent State:
 *    Easy to forget resetting state in one branch, leaving machine stuck in DISPENSING.
 */
public class VendingMachine {

    private State state;
    private int count;

    public VendingMachine(int count) {
        this.count = count;
        this.state = count > 0 ? State.NO_COIN : State.SOLD_OUT;
    }

    public State getState() {
        return state;
    }

    public int getCount() {
        return count;
    }

    public void insertCoin() {
        switch (state) {
            case NO_COIN -> {
                System.out.println("Coin accepted. Please turn crank.");
                state = State.HAS_COIN;
            }
            case HAS_COIN -> System.out.println("Coin already inserted! Ejecting extra coin.");
            case DISPENSING -> System.out.println("Please wait, dispensing item.");
            case SOLD_OUT -> System.out.println("Machine is sold out! Returning inserted coin.");
        }
    }

    public void ejectCoin() {
        switch (state) {
            case HAS_COIN -> {
                System.out.println("Coin returned.");
                state = State.NO_COIN;
            }
            case NO_COIN -> System.out.println("No coin inserted.");
            case DISPENSING -> System.out.println("Cannot return coin, item already being dispensed.");
            case SOLD_OUT -> System.out.println("No coin to return.");
        }
    }

    public void turnCrank() {
        switch (state) {
            case HAS_COIN -> {
                System.out.println("Crank turned. Dispensing item...");
                state = State.DISPENSING;
                dispense();
            }
            case NO_COIN -> System.out.println("Please insert a coin first.");
            case DISPENSING -> System.out.println("Already dispensing, turning crank does nothing.");
            case SOLD_OUT -> System.out.println("Sold out, crank turned does nothing.");
        }
    }

    private void dispense() {
        if (state == State.DISPENSING) {
            count--;
            System.out.println("A snack rolls into the tray.");
            if (count > 0) {
                state = State.NO_COIN;
            } else {
                System.out.println("Machine is now out of stock!");
                state = State.SOLD_OUT;
            }
        }
    }
}
