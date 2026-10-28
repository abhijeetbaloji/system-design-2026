package com.systemdesign.lld.patterns.state.example01.good;

/**
 * Concrete State: Coin has been inserted and accepted.
 */
public class HasCoinState implements VendingState {

    private final VendingMachine machine;

    public HasCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("A coin is already inserted! Ejecting extra coin.");
    }

    @Override
    public void ejectCoin() {
        System.out.println("Coin returned to coin return slot.");
        machine.setState(machine.getNoCoinState());
    }

    @Override
    public void turnCrank() {
        System.out.println("Crank turned. Processing your selection...");
        machine.setState(machine.getDispensingState());
    }

    @Override
    public void dispense() {
        System.out.println("Turn the crank to complete item dispense.");
    }

    @Override
    public String getStateName() {
        return "HAS_COIN";
    }
}
