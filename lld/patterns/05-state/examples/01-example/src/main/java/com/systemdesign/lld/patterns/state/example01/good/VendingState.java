package com.systemdesign.lld.patterns.state.example01.good;

/**
 * State Interface:
 * Declares all actions supported by the VendingMachine across its lifecycle.
 */
public interface VendingState {

    void insertCoin();

    void ejectCoin();

    void turnCrank();

    void dispense();

    String getStateName();
}
