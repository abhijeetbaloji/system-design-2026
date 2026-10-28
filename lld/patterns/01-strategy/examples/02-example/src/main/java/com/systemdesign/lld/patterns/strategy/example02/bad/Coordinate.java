package com.systemdesign.lld.patterns.strategy.example02.bad;

/**
 * Geographical coordinate.
 */
public record Coordinate(double latitude, double longitude) {
    public Coordinate {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Latitude out of bounds: " + latitude);
        }
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Longitude out of bounds: " + longitude);
        }
    }

    /**
     * Approximate Euclidean distance in kilometers (simplified for demo).
     */
    public double distanceTo(Coordinate other) {
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371.0 * c; // Earth's radius in km
    }
}
