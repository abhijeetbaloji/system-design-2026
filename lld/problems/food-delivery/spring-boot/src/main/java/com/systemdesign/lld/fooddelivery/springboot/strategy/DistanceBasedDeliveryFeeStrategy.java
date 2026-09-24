package com.systemdesign.lld.fooddelivery.springboot.strategy;

import com.systemdesign.lld.fooddelivery.springboot.domain.Location;

public class DistanceBasedDeliveryFeeStrategy implements DeliveryFeeStrategy {
    private final double baseFee;
    private final double perKmRate;
    private final double freeDeliveryThreshold;

    public DistanceBasedDeliveryFeeStrategy(double baseFee, double perKmRate, double freeDeliveryThreshold) {
        this.baseFee = baseFee;
        this.perKmRate = perKmRate;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }

    public DistanceBasedDeliveryFeeStrategy() {
        this(20.0, 5.0, 1000.0);
    }

    @Override
    public double calculateFee(Location restaurantLocation, Location customerLocation, double subtotal) {
        if (subtotal >= freeDeliveryThreshold) {
            return 0.0;
        }
        double distance = restaurantLocation.distanceTo(customerLocation);
        return baseFee + (distance * perKmRate);
    }
}
