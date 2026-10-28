package com.systemdesign.lld.patterns.strategy.example02.bad;

/**
 * Demo runner for bad Navigator design.
 */
public class RouteDemo {
    public static void main(String[] args) {
        Coordinate downtown = new Coordinate(40.7128, -74.0060);
        Coordinate uptown = new Coordinate(40.7829, -73.9654);

        Navigator navigator = new Navigator();

        Route driving = navigator.buildRoute(downtown, uptown, TravelMode.DRIVING);
        System.out.println("Driving: " + driving.summary() + " (" + driving.durationMinutes() + " mins, " + String.format("%.2f", driving.distanceKm()) + " km)");

        Route walking = navigator.buildRoute(downtown, uptown, TravelMode.WALKING);
        System.out.println("Walking: " + walking.summary() + " (" + walking.durationMinutes() + " mins, " + String.format("%.2f", walking.distanceKm()) + " km)");
    }
}
