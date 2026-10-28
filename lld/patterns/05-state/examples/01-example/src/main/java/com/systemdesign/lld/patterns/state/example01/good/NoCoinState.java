package com.systemdesign.lld.patterns.state.example01.good;

/**
 * Concrete State: Machine is waiting for a coin insertion.
 */
public class NoCoinState implements VendingState {

    private final VendingMachine machine;

    public NoCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("Coin accepted. You can now turn the crank.");
        machine.setState(machine.getHasCoinState());
    }

    @Override
    public void ejectCoin() {
        System.out.println("No coin has been inserted yet.");
    }

    @Override
    public void turnCrank() {
        System.out.println("You turned the crank, but there's no coin inserted.");
    }

    @Override
    public void dispense() {
        System.out.println("Payment required before dispensing.");
    }

    @Override
    public String getStateName() {
        return "NO_COIN";
    }
}
