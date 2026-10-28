package com.systemdesign.lld.patterns.state.example03.good;

/**
 * Concrete State: Physical keypad / touchscreen is locked.
 * Retains a reference to the previous state to restore it upon unlock.
 */
public class LockedState implements PlayerState {

    private final PlayerState previousState;

    public LockedState(PlayerState previousState) {
        this.previousState = previousState;
    }

    public void unlock(MediaPlayer player) {
        System.out.println("Hardware: Keypad unlocked. Restoring state: " + previousState.getStateName());
        player.setState(previousState);
    }

    @Override
    public void play(MediaPlayer player) {
        System.out.println("LOCKED: Controls disabled. Unlock device to play.");
    }

    @Override
    public void pause(MediaPlayer player) {
        System.out.println("LOCKED: Controls disabled.");
    }

    @Override
    public void stop(MediaPlayer player) {
        System.out.println("LOCKED: Controls disabled.");
    }

    @Override
    public void next(MediaPlayer player) {
        System.out.println("LOCKED: Controls disabled.");
    }

    @Override
    public void previous(MediaPlayer player) {
        System.out.println("LOCKED: Controls disabled.");
    }

    @Override
    public String getStateName() {
        return "LOCKED";
    }

    public PlayerState getPreviousState() {
        return previousState;
    }
}
