package com.systemdesign.lld.fooddelivery.interview.strategy.fee;

import com.systemdesign.lld.fooddelivery.interview.domain.Location;

/*
 * Design Intent:
 * Standard linear distance-based delivery fee calculation:
 * fee = baseFee + (distance * perKmRate).
 * If subtotal exceeds freeDeliveryThreshold, fee is waived (0.0).
 */
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
