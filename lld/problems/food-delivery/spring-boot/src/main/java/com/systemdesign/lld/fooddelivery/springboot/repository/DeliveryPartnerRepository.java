package com.systemdesign.lld.fooddelivery.springboot.repository;

import com.systemdesign.lld.fooddelivery.springboot.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.springboot.domain.PartnerStatus;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
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
