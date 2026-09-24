package com.systemdesign.lld.fooddelivery.good;

/*
 * Design Intent:
 * Location encapsulates geographic coordinates and provides a domain method for
 * distance computation, eliminating primitive obsession (raw double pairs).
 * Implemented as an immutable record for thread safety and value equality.
 */
public record Location(double latitude, double longitude) {

    public double distanceTo(Location other) {
        if (other == null) return 0.0;
        // Euclidean distance approximation for in-memory LLD
        return Math.sqrt(Math.pow(this.latitude - other.latitude, 2) + Math.pow(this.longitude - other.longitude, 2));
    }
}
