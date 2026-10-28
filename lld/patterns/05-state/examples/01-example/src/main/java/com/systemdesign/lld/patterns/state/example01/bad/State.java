package com.systemdesign.lld.patterns.state.example01.bad;

/**
 * State enum for bad vending machine design.
 */
public enum State {
    NO_COIN,
    HAS_COIN,
    DISPENSING,
    SOLD_OUT
}
