package com.systemdesign.lld.fooddelivery.interview.repository;

import com.systemdesign.lld.fooddelivery.interview.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.interview.domain.PartnerStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Design Intent:
 * DeliveryPartnerRepository manages delivery partner records and allows querying
 * currently available couriers for dispatching.
 */
public class DeliveryPartnerRepository {
    private final Map<String, DeliveryPartner> store = new ConcurrentHashMap<>();

    public void save(DeliveryPartner partner) {
        if (partner != null) {
            store.put(partner.getId(), partner);
        }
    }

    public Optional<DeliveryPartner> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<DeliveryPartner> findAvailablePartners() {
        return store.values().stream()
                .filter(p -> p.getStatus() == PartnerStatus.AVAILABLE)
                .toList();
    }

    public Collection<DeliveryPartner> findAll() {
        return store.values();
    }
}
