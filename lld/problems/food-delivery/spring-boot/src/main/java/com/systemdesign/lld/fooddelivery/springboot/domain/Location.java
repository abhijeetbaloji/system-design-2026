package com.systemdesign.lld.fooddelivery.springboot.domain;

public record Location(double latitude, double longitude) {
    public double distanceTo(Location other) {
        if (other == null) return 0.0;
        return Math.sqrt(Math.pow(this.latitude - other.latitude, 2) + Math.pow(this.longitude - other.longitude, 2));
    }
}
