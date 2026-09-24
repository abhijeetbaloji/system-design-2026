package com.systemdesign.lld.fooddelivery.interview.strategy.dispatch;

import com.systemdesign.lld.fooddelivery.interview.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.interview.domain.Location;

import java.util.List;
import java.util.Optional;

/*
 * Design Intent (Strategy Pattern):
 * DeliveryPartnerMatchingStrategy abstracts the matching heuristic used to select
 * an optimal delivery partner for an order from a pool of available candidates.
 * Solves OCP: dispatch algorithms can change (nearest, highest-rated, batching)
 * without modifying order or dispatch workflows.
 */
public interface DeliveryPartnerMatchingStrategy {
    Optional<DeliveryPartner> matchPartner(List<DeliveryPartner> availablePartners, Location pickupLocation);
}
