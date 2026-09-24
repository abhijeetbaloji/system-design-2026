package com.systemdesign.lld.fooddelivery.springboot.strategy;

import com.systemdesign.lld.fooddelivery.springboot.domain.Location;

import java.util.Objects;

public class SurgeDeliveryFeeStrategy implements DeliveryFeeStrategy {
    private final DeliveryFeeStrategy baseStrategy;
    private final double surgeMultiplier;

    public SurgeDeliveryFeeStrategy(DeliveryFeeStrategy baseStrategy, double surgeMultiplier) {
        this.baseStrategy = Objects.requireNonNull(baseStrategy, "Base DeliveryFeeStrategy cannot be null");
        if (surgeMultiplier < 1.0) throw new IllegalArgumentException("Surge multiplier cannot be less than 1.0");
        this.surgeMultiplier = surgeMultiplier;
    }

    @Override
    public double calculateFee(Location restaurantLocation, Location customerLocation, double subtotal) {
        return baseStrategy.calculateFee(restaurantLocation, customerLocation, subtotal) * surgeMultiplier;
    }
}
