package com.systemdesign.lld.patterns.mediator.example02.bad;

/**
 * Flawed design: Runway exposes its state directly and is manipulated by aircraft.
 */
public class BadRunway {
    private final String id;
    private boolean occupied;
    private String currentOccupant;

    public BadRunway(String id) {
        this.id = id;
        this.occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied, String occupant) {
        this.occupied = occupied;
        this.currentOccupant = occupant;
    }

    public String getId() {
        return id;
    }

    public String getCurrentOccupant() {
        return currentOccupant;
    }
}
