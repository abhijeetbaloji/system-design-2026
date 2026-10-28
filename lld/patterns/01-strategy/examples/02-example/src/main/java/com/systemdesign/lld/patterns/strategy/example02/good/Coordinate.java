package com.systemdesign.lld.patterns.strategy.example02.good;

/**
 * Immutable geographical coordinate value object.
 */
public record Coordinate(double latitude, double longitude) {
    public Coordinate {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90, got: " + latitude);
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180, got: " + longitude);
        }
    }

    /**
     * Computes the Great-Circle distance using the Haversine formula (in kilometers).
     */
    public double distanceTo(Coordinate other) {
        if (other == null) {
            throw new IllegalArgumentException("Target coordinate cannot be null");
        }
        if (this.equals(other)) {
            return 0.0;
        }

        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371.0 * c; // Earth radius in km
    }
}
