package com.systemdesign.lld.fooddelivery.springboot.strategy;

import com.systemdesign.lld.fooddelivery.springboot.domain.Location;

public interface DeliveryFeeStrategy {
    double calculateFee(Location restaurantLocation, Location customerLocation, double subtotal);
}
