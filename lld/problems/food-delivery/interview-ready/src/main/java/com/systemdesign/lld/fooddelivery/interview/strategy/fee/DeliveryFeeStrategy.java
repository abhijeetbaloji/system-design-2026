package com.systemdesign.lld.fooddelivery.interview.strategy.fee;

import com.systemdesign.lld.fooddelivery.interview.domain.Location;

/*
 * Design Intent (Strategy Pattern):
 * DeliveryFeeStrategy encapsulates algorithms for calculating delivery fees.
 * Solves OCP: new pricing schemes (surge, distance-based, weather-based) can be plugged in
 * without modifying checkout or order domain classes.
 */
public interface DeliveryFeeStrategy {
    double calculateFee(Location restaurantLocation, Location customerLocation, double subtotal);
}
