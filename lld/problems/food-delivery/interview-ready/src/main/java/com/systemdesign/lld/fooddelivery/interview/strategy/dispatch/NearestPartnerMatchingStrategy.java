package com.systemdesign.lld.fooddelivery.interview.strategy.dispatch;

import com.systemdesign.lld.fooddelivery.interview.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.interview.domain.Location;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/*
 * Design Intent:
 * Matches the physically closest available delivery partner to the pickup location.
 * Minimizes pickup transit time.
 */
public class NearestPartnerMatchingStrategy implements DeliveryPartnerMatchingStrategy {

    @Override
    public Optional<DeliveryPartner> matchPartner(List<DeliveryPartner> availablePartners, Location pickupLocation) {
        if (availablePartners == null || availablePartners.isEmpty() || pickupLocation == null) {
            return Optional.empty();
        }
        return availablePartners.stream()
                .min(Comparator.comparingDouble(partner -> partner.getLocation().distanceTo(pickupLocation)));
    }
}
