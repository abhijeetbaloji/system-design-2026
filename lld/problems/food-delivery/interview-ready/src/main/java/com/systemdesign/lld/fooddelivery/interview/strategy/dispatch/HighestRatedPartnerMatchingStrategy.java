package com.systemdesign.lld.fooddelivery.interview.strategy.dispatch;

import com.systemdesign.lld.fooddelivery.interview.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.interview.domain.Location;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/*
 * Design Intent:
 * Matches the highest-rated available delivery partner, breaking ties with proximity.
 * Prioritizes customer satisfaction and service quality.
 */
public class HighestRatedPartnerMatchingStrategy implements DeliveryPartnerMatchingStrategy {

    @Override
    public Optional<DeliveryPartner> matchPartner(List<DeliveryPartner> availablePartners, Location pickupLocation) {
        if (availablePartners == null || availablePartners.isEmpty() || pickupLocation == null) {
            return Optional.empty();
        }
        return availablePartners.stream()
                .max(Comparator.comparingDouble(DeliveryPartner::getRating)
                        .thenComparing((p1, p2) -> Double.compare(
                                p2.getLocation().distanceTo(pickupLocation),
                                p1.getLocation().distanceTo(pickupLocation))));
    }
}
