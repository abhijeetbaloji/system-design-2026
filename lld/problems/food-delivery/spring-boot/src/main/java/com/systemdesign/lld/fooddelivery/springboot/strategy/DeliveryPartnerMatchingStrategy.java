package com.systemdesign.lld.fooddelivery.springboot.strategy;

import com.systemdesign.lld.fooddelivery.springboot.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.springboot.domain.Location;

import java.util.List;
import java.util.Optional;

public interface DeliveryPartnerMatchingStrategy {
    Optional<DeliveryPartner> matchPartner(List<DeliveryPartner> availablePartners, Location pickupLocation);
}
