package com.systemdesign.lld.fooddelivery.springboot.strategy;

import com.systemdesign.lld.fooddelivery.springboot.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.springboot.domain.Location;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class NearestPartnerMatchingStrategy implements DeliveryPartnerMatchingStrategy {

    @Override
    public Optional<DeliveryPartner> matchPartner(List<DeliveryPartner> availablePartners, Location pickupLocation) {
        if (availablePartners == null || availablePartners.isEmpty() || pickupLocation == null) {
            return Optional.empty();
        }
        return availablePartners.stream()
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceTo(pickupLocation)));
    }
}
