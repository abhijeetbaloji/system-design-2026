package com.systemdesign.lld.patterns.strategy.example02.good;

/**
 * Demonstrates the Strategy pattern for route navigation.
 */
public class RouteDemo {
    public static void main(String[] args) {
        Coordinate downtown = new Coordinate(40.7128, -74.0060);
        Coordinate uptown = new Coordinate(40.7829, -73.9654);

        Navigator navigator = new Navigator(new DrivingRouteStrategy());

        // 1. Driving Route
        Route driving = navigator.planRoute(downtown, uptown);
        printRoute("1. DRIVING", driving);

        // 2. Switch to Walking Strategy
        navigator.setRouteStrategy(new WalkingRouteStrategy());
        Route walking = navigator.planRoute(downtown, uptown);
        printRoute("2. WALKING", walking);

        // 3. Switch to Bicycling Strategy
        navigator.setRouteStrategy(new BicyclingRouteStrategy());
        Route cycling = navigator.planRoute(downtown, uptown);
        printRoute("3. BICYCLING", cycling);

        // 4. Switch to Public Transit Strategy
        navigator.setRouteStrategy(new PublicTransitRouteStrategy());
        Route transit = navigator.planRoute(downtown, uptown);
        printRoute("4. PUBLIC TRANSIT", transit);

        // 5. Custom Lambda Strategy: Helicopter direct flight!
        navigator.setRouteStrategy((orig, dest) -> {
            double distance = orig.distanceTo(dest);
            int minutes = Math.max(1, (int) Math.round((distance / 220.0) * 60.0));
            return new Route(distance, minutes, 250.0, "Direct Helicopter Charter", java.util.List.of("Takeoff from helipad", "Direct flight", "Land at rooftop"));
        });
        Route helicopter = navigator.planRoute(downtown, uptown);
        printRoute("5. HELICOPTER (LAMBDA)", helicopter);
    }

    private static void printRoute(String modeTitle, Route route) {
        System.out.println("=== " + modeTitle + " ===");
        System.out.println("Summary:  " + route.summary());
        System.out.println("Distance: " + String.format("%.2f", route.distanceKm()) + " km");
        System.out.println("Duration: " + route.durationMinutes() + " mins");
        System.out.println("Cost:     $" + String.format("%.2f", route.estimatedCost()));
        System.out.println("Steps:    " + route.turnByTurnSteps().size() + " directions");
        System.out.println();
    }
}
