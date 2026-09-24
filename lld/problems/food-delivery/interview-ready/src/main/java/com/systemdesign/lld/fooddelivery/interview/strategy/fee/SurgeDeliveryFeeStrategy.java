package com.systemdesign.lld.fooddelivery.interview.strategy.fee;

import com.systemdesign.lld.fooddelivery.interview.domain.Location;

import java.util.Objects;

/*
 * Design Intent (Decorator / Strategy Variation):
 * SurgeDeliveryFeeStrategy decorates an underlying DeliveryFeeStrategy and applies
 * a peak-hour surge multiplier (e.g. 1.5x during rain or peak lunch/dinner rushes).
 */
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
        double baseFee = baseStrategy.calculateFee(restaurantLocation, customerLocation, subtotal);
        return baseFee * surgeMultiplier;
    }
}
