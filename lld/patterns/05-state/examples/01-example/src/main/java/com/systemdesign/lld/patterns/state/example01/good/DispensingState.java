package com.systemdesign.lld.patterns.state.example01.good;

/**
 * Concrete State: The machine is actively rotating the spiral coil to dispense an item.
 */
public class DispensingState implements VendingState {

    private final VendingMachine machine;

    public DispensingState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("Please wait, already dispensing an item.");
    }

    @Override
    public void ejectCoin() {
        System.out.println("Sorry, you already turned the crank; item is being dispensed.");
    }

    @Override
    public void turnCrank() {
        System.out.println("Turning twice does not give you another item!");
    }

    @Override
    public void dispense() {
        machine.releaseItem();
        if (machine.getCount() > 0) {
            machine.setState(machine.getNoCoinState());
        } else {
            System.out.println("Oops, out of stock!");
            machine.setState(machine.getSoldOutState());
        }
    }

    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}
